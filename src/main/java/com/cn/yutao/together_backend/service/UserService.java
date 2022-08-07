package com.cn.yutao.together_backend.service;

import cn.hutool.core.util.IdUtil;
import com.cn.yutao.together_backend.entity.User;
import com.cn.yutao.together_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User createUser(User user) {
        user.setIdentifyCode(generateIdCode());
        return userRepository.save(user);
    }

    private String generateIdCode() {
        var idCode = IdUtil.fastSimpleUUID().substring(0, 6).toUpperCase();
        final var identifyCodeUserOptional = userRepository.findByIdentifyCode(idCode);

        if (identifyCodeUserOptional.isPresent()) {
            idCode = generateIdCode();
        }
        return idCode;
    }
}
