

# jwt的用户验证

## 1.生成token

采用jwt自带的工具类生成token。

```java
// 生成JWT token
public String generateToken(Long userId, String username, String role) {
    return Jwts.builder()
            .setSubject(userId.toString())
            .claim("username", username)
            .claim("role", role)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(key)
            .compact();
}
```

其中最重要的就是`setSubject()`和`claim()`

前者设置一个主键，后者以map形式设置你想存储的键值对。

## 2.验证token

这里就需要用到interceptor

重写preHandle，在controller前进行验证。

```java
public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Authorization");
    
        // 登录验证
        if (token == null) {
            throw new BusinessException(401, "请先登录");
        }
    
        // 过期验证
        if (!jwtUtil.validateToken(token)) {
            throw new BusinessException(401, "登录已过期，请重新登录");
        }
    
        // 将用户ID存入请求属性
        // 方便后续获得
        Long userId = jwtUtil.getUserIdFromToken(token);
        String role = jwtUtil.getRoleFromToken(token);
        request.setAttribute("role", role);
        request.setAttribute("userId", userId);
        return true;
    }
}
```

