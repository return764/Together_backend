package com.cn.yutao.together_backend.exception.base;

public class BaseBadRequestException extends RuntimeException {
    public BaseBadRequestException(String message) {
        super(message);
    }
}
