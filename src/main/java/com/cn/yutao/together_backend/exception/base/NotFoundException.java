package com.cn.yutao.together_backend.exception.base;

public class NotFoundException extends BaseBadRequestException {
    public NotFoundException(String message) {
        super(message);
    }
}
