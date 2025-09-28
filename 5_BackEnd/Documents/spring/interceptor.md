# interceptor的作用

### 拦截请求，在请求到达controller之前或者之后对请求进行处理，如：

- 登录信息验证
- 身份校验

## 使用的步骤

### 1.创建拦截器类(@Component)，如：
```java
@Component
public class JwtInterceptor implements HandlerInterceptor {

}
```
### 2.根据拦截器起作用的时期,重写preHandle、postHandle、afterCompletion方法，如：

```java
@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    String token = request.getHeader("Authorization");

    // 终端打印出token
    if (token == null) {
        throw new BusinessException(401, "请先登录");
    }

    // 验证token
    if (!jwtUtil.validateToken(token)) {
        throw new BusinessException(401, "登录已过期，请重新登录");
    }

    // 将用户ID存入请求属性
    Long userId = jwtUtil.getUserIdFromToken(token);
    String role = jwtUtil.getRoleFromToken(token);
    request.setAttribute("role", role);
    request.setAttribute("userId", userId);
    return true;
}
```

### 3.在WebConfig中配置需要拦截的url，如：

```java
@Override
public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(jwtInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/user/login", "/user/register")
            .order(1);
```

## 其他:

### 关于多个拦截器的执行顺序:



    preHandle 按注册顺序执行

    Controller 方法 执行

    postHandle 按注册逆序执行

    afterCompletion 按注册逆序执行

### 如何注册:

```java
@Override
public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(jwtInterceptor)        
            .order(1);
    // preHandle 1最先执行
    // postHandle, afterCompletion 1最后执行    
```