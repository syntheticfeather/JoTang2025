package com.example.demo.exception;

public class BusinessException extends BaseException {

    public BusinessException(Integer code, String message) {
        super(code, message);
    }

    public BusinessException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
