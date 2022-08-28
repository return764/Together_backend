package com.cn.yutao.together_backend.exception;

import com.cn.yutao.together_backend.exception.base.NotFoundException;

public class TaskNotFoundException extends NotFoundException {
    public TaskNotFoundException(String message) {
        super(message);
    }
}
