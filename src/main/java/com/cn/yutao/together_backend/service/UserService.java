package com.cn.yutao.together_backend.service;

import com.cn.yutao.together_backend.entity.User;
import com.cn.yutao.together_backend.exception.UserNotFoundException;
import com.cn.yutao.together_backend.repository.UserRepository;
import com.cn.yutao.together_backend.utils.IdentifyCodeUtils;
import com.cn.yutao.together_backend.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;


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

    public User fetchByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User " + username + " not found."));
    }

    public User login(UsernamePasswordAuthenticationToken token) {
        final UserDetails userDetails = SecurityUtils.login(token, authenticationManager);
        return fetchByUsername(userDetails.getUsername());
    }

    public User fetchByIdentifyCode(String identifyCode) {
        return userRepository.findByIdentifyCode(identifyCode).orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    public void bind(User bindUser) {
        final User loginUser = SecurityUtils.getLoginUser();
        loginUser.setBinding(bindUser);
        userRepository.save(loginUser);
        bindUser.setBinding(loginUser);
        userRepository.save(bindUser);
    }

    public User fetchById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found."));
    }
}
