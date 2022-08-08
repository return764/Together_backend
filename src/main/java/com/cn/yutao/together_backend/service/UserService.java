package com.cn.yutao.together_backend.service;

import com.cn.yutao.together_backend.entity.User;
import com.cn.yutao.together_backend.repository.UserRepository;
import com.cn.yutao.together_backend.utils.IdentifyCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User createUser(User user) {
        user.setIdentifyCode(generateIdCode());
        return userRepository.save(user);
    }

    private String generateIdCode() {
        var idCode = IdentifyCodeUtils.genIdCode();
        final var identifyCodeUserOptional = userRepository.findByIdentifyCode(idCode);

        if (identifyCodeUserOptional.isPresent()) {
            idCode = generateIdCode();
        }
        return idCode;
    }
}
