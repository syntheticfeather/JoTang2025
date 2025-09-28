# 参数校验

## 使用步骤

### 1.在java实体类中加入参数的校验注解(@NotBlank、@Size、@Pattern)，如：

```java
public class RegisterRequest {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20个字符之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码长度至少6位")
    private String password;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    private Boolean AdminRegister;
}
```

### 2.在controller中加入@valid注解，进行参数校验，如：

```java
@PostMapping("/register")
public ResponseEntity<ApiResponse<User>> register(@Valid @RequestBody RegisterRequest request) {
    User user = userService.register(request);
    return ResponseEntity.ok(ApiResponse.success(user, "注册成功"));
}
```

### 3.使用全局异常处理器，对参数校验失败的异常(MethodArgumentNotValidException.class)进行处理，返回统一的响应格式。

```java
// 处理参数验证异常（如@Validated失败）
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException e) {
    log.warn("参数验证失败: {}", e.getMessage());

    // 提取所有字段错误信息
    StringBuilder errorMessage = new StringBuilder();
    e.getBindingResult().getFieldErrors().forEach(error -> {
        if (errorMessage.length() > 0) {
            errorMessage.append("; ");
        }
        errorMessage.append(error.getDefaultMessage());
    });

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(400, errorMessage.toString()));
}
```