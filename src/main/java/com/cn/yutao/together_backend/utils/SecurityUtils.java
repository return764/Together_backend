package com.cn.yutao.together_backend.utils;

import com.cn.yutao.together_backend.entity.User;
import com.cn.yutao.together_backend.integration.security.UserAuthentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtils {
    private SecurityUtils() {
    }

    public static UserDetails login(UsernamePasswordAuthenticationToken token, AuthenticationManager manager) {
        final var authentication = manager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return (UserDetails) authentication.getPrincipal();
    }

    public static User getLoginUser() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        final var userAuthentication = (UserAuthentication) authentication.getPrincipal();
        return userAuthentication.getLoginUser();
    }
}
