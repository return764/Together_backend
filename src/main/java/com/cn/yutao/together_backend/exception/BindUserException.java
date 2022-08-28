package com.cn.yutao.together_backend.exception;

import com.cn.yutao.together_backend.exception.base.BaseBadRequestException;

public class BindUserException extends BaseBadRequestException {
    public BindUserException(String message) {
        super(message);
    }
}
