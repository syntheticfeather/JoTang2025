# 关于所有异常类的文档

## 1.BusinessException

业务异常类，用于业务逻辑异常。

## 2.ResourceNotFoundException

资源未找到异常类，用于资源不存在的异常。

## 3.MethodArgumentNotValidException

方法参数无效异常类，用于方法参数校验失败的异常。


# 异常抛出

## 使用步骤

### 1.自定义各种异常类

```java
public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(String message) {
        super(404, message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(404, message, cause);
    }
}
```
如ResouceNotFoundException、InvalidParameterException等。

### 2.使用全局异常处理器，处理不同的异常，然后返回统一的响应格式

```java
// 异常处理类的创建注解
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 局部异常处理方法的创建注解
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Object>> handleBaseException(BaseException e) {
        // 对不同异常的处理逻辑
        log.warn("业务异常: {}", e.getMessage());
        // 统一返回格式
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getCode(), e.getMessage()));
    }

    ......
}
```