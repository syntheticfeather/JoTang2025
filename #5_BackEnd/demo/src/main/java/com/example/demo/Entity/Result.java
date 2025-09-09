package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Result<T> {

    Integer code;

    String message;

    T data;

    public static <T> Result<T> Success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    public static <T> Result<T> Fail(T data) {
        return new Result<>(500, "操作失败", data);
    }

}
