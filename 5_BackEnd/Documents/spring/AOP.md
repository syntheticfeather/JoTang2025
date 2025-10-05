# AOP (Aspect Oriented Programming) 面向切面编程

Spring的第二核心内容就是AOP (Aspect Oriented Programming)

# 传统方法和SpringAOP方法的比较

## 传统方法:

```java
public class BookService {
    public void createBook(Book book) {
        securityCheck();
        Transaction tx = startTransaction();
        try {
            // 核心业务逻辑
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        }
        log("created book: " + book);
    }

    public void updateBook(Book book) {
    securityCheck();
    Transaction tx = startTransaction();
    try {
        // 核心业务逻辑
        tx.commit();
    } catch (RuntimeException e) {
        tx.rollback();
        throw e;
    }
    log("updated book: " + book);
    }
}
```

可以看到每个方法里面其实核心业务的逻辑很少，全是其他的逻辑，比如安全检查、事务处理、日志记录等。

* 核心业务逻辑

* 切面逻辑

    * 安全检查

    * 日志

    * 事务处理

## Spring的AOP方法:

```java
@Aspect
@Component
public class LoggingAspect {
    // 在执行UserService的每个方法前执行:
    @Before("execution(public * com.itranswarp.learnjava.service.UserService.*(..))")
    public void doAccessCheck() {
        System.err.println("[Before] do access check...");
    }

    // 在执行MailService的每个方法前后执行:
    @Around("execution(public * com.itranswarp.learnjava.service.MailService.*(..))")
    public Object doLogging(ProceedingJoinPoint pjp) throws Throwable {
        System.err.println("[Around] start " + pjp.getSignature());
        Object retVal = pjp.proceed();
        System.err.println("[Around] done " + pjp.getSignature());
        return retVal;
    }
}

@Configuration
@ComponentScan
// 注册类中加上自动代理
@EnableAspectJAutoProxy
public class AppConfig {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);
        User user = userService.login("bob@example.com", "password");

        MailService mailService = context.getBean(MailService.class);
        System.out.println(mailService.getTime());
    }
}
```

![](imgs/AOP1.png "切面编程")

# AOP的全部注解

|注解|功能|
|-|-|
|@Before：|这种拦截器先执行拦截代码，再执行目标代码。如果拦截器抛异常，那么目标代码就不执行了；|
|@After：|这种拦截器先执行目标代码，再执行拦截器代码。无论目标代码是否抛异常，拦截器代码都会执行；|
|@AfterReturning：|和@After不同的是，只有当目标代码正常返回时，才执行拦截器代码；|
|@AfterThrowing：|和@After不同的是，只有当目标代码抛出了异常时，才执行拦截器代码；|
|@Around：|能完全控制目标代码是否执行，并可以在执行前后、抛异常后执行任意拦截代码，可以说是包含了上面所有功能。|

# AOP的注解使用

可以看到我们刚才的@Aspect类中@Before和@Around()里面写了一长串的AspectJ表达式，这些表达式就是切面的定义，

但是这样显然很复杂，而且这样书写，意味着类中的所有public方法都会进行切面逻辑的处理，但实际上我们并不想如此.

这时我们就想到了事务处理的Transactional注解，我们也这样实现。

## 所以进行注解式切面引用。

比如我们对检测方法性能的功能进行切面编程

```java
// 先编写AOP切面
@Component
@Aspect
public class PerformanceAspect {

    @Around("@annotation(performanceAnnotation)")
    public Object logPerformance(ProceedingJoinPoint joinPoint, PerformanceMonitor performanceAnnotation) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        System.out.println(performanceAnnotation.value() + " took " + (end - start) + " ms");
        return result;
    }

    // 或者用try写法
    @Around("@annotation(myAnnotation)")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint, MyAnnotation myAnnotation) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            // 执行目标方法
            Object result = joinPoint.proceed();
            return result;
        } finally {
            long duration = System.currentTimeMillis() - start;
            System.out.println(joinPoint.getSignature() + " executed in " + duration + "ms");
        }
    }
}

// 仿照Transactional注解编写注解
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PerformanceMonitor {

    public String value() default "";
}

// 然后需要的地方进行调用
@Component
public class CalImpl {

    @MyAnnotation("[累加函数]")
    public int addRange(int a, int b) {
        int sum = 0;
        for (int i = a; i <= b; i++) {
            sum += i;
        }
        return sum;
    }
}
```

### 注意Aspect中的@Around，语法是:
```java
"@annotation(xxx)"
public Object aroundAdvice(ProceedingJoinPoint joinPoint, MyAnnotation xxx) throws Throwable {
    ...
}
```

其实里面写的什么就是你的形式变量名，真正绑定什么注解，其实是**函数里的参数类型**决定的。

---

### Try语句不用和用的区别是什么呢

**用Try语句** 在遭遇异常的情况仍然会输出性能结果   
**不用Try语句** 就会直接抛出异常跑路