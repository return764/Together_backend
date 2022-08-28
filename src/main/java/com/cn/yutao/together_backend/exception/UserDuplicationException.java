package com.cn.yutao.together_backend.exception;

import com.cn.yutao.together_backend.exception.base.BaseBadRequestException;

public class UserDuplicationException extends BaseBadRequestException {
    public UserDuplicationException(String username) {
        super(String.format("用户名 %s 已被注册", username));
    }
}
