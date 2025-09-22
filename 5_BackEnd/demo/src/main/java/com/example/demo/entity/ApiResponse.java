package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private Integer code;
    private String message;
    private T data;
    private Long timestamp;

    // 成功响应 - 不带消息
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "成功", data, System.currentTimeMillis());
    }

    // 成功响应 - 带自定义消息
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(200, message, data, System.currentTimeMillis());
    }

    // 错误响应
    public static <T> ApiResponse<T> error(Integer code, String message) {
        return new ApiResponse<>(code, message, null, System.currentTimeMillis());
    }

    // 错误响应 - 带数据
    public static <T> ApiResponse<T> error(Integer code, String message, T data) {
        return new ApiResponse<>(code, message, data, System.currentTimeMillis());
    }
}
