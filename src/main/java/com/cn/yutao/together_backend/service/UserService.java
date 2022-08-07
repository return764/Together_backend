package com.cn.yutao.together_backend.service;

import cn.hutool.core.util.IdUtil;
import com.cn.yutao.together_backend.entity.User;
import com.cn.yutao.together_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User createUser(User user) {
        final var idCode = IdUtil.fastSimpleUUID().substring(0, 6).toUpperCase();
        user.setIdentifyCode(idCode);
        return userRepository.save(user);
    }
}
