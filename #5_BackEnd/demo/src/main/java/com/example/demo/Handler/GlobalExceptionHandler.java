package com.example.demo.Handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.Entity.Result;
import com.example.demo.Exception.ResourceNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        e.printStackTrace();
        return Result.Fail(e.getMessage().length() == 0 ? "操作失败" : e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public Result handleResourceNotFoundException(ResourceNotFoundException e) {
        e.printStackTrace();
        return Result.Fail(e.getMessage().length() == 0 ? "资源不存在" : e.getMessage());
    }
}
