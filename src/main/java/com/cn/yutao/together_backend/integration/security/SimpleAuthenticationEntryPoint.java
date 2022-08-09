package com.cn.yutao.together_backend.integration.security;

import com.cn.yutao.together_backend.exception.ErrorResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SimpleAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ErrorResult errorResult = new ErrorResult();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        errorResult.setMessage(authException.getMessage());
        authException.printStackTrace();
        ObjectMapper objectMapper = new ObjectMapper();
        try (final var writer = response.getWriter()) {
            writer.print(objectMapper.writeValueAsString(errorResult));
            writer.flush();
        }
    }
}
