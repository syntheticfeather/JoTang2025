package com.example.spring_test.Aspect.test;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyAspect {

    // 环绕通知示例：测量方法执行时间
    @Around("@annotation(myAnnotation)")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint, MyAnnotation myAnnotation) throws Throwable {
        String name = myAnnotation.value() == "" ? joinPoint.getSignature().getName() : myAnnotation.value();
        long start = System.currentTimeMillis();
        try {
            // 执行目标方法
            Object result = joinPoint.proceed();
            return result;
        } finally {
            long duration = System.currentTimeMillis() - start;
            System.out.println(name + " executed in " + duration + "ms");
        }
    }
}
