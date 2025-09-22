package com.example.demo.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final Integer code;

    public BaseException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
