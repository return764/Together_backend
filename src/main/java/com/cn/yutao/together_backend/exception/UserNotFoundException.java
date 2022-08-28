package com.cn.yutao.together_backend.exception;

import com.cn.yutao.together_backend.exception.base.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
