package com.cn.yutao.together_backend.exception;


import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResult handleValidArgumentsException(MethodArgumentNotValidException e) {
        final var errMessage = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(";"));
        return new ErrorResult(errMessage);
    }

    @ExceptionHandler(BindUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResult handleBindUserException(BindUserException e) {
        return new ErrorResult(e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ErrorResult handleBadCredentialsException(BadCredentialsException e) {
        return new ErrorResult(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResult handleOtherException(Exception e) {
        log.error(e.getMessage());
        return new ErrorResult(e.getMessage());
    }
}
