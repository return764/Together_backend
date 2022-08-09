package com.cn.yutao.together_backend.service;

import com.cn.yutao.together_backend.entity.User;
import com.cn.yutao.together_backend.repository.UserRepository;
import com.cn.yutao.together_backend.utils.IdentifyCodeUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        user.setIdentifyCode(generateIdCode());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
