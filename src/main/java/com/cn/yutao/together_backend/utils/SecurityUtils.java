package com.cn.yutao.together_backend.utils;

import com.cn.yutao.together_backend.entity.User;
import com.cn.yutao.together_backend.integration.security.UserAuthentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    public static void refresh(User refreshedUser) {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        final var principal =(UserAuthentication) authentication.getPrincipal();
        // reload user
        principal.withLoginUser(refreshedUser);
        // re-create authentication
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                principal, authentication.getCredentials(), authentication.getAuthorities()
        );
        token.setDetails(authentication.getDetails());
        // save authentication
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
