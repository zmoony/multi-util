package com.boot.shiro.httpservice.openfeign;

public class HystrixFallbackException extends RuntimeException {
    public HystrixFallbackException(String message) {
        super(message);
    }
}
