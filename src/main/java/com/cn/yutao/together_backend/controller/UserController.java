package com.cn.yutao.together_backend.controller;


import com.cn.yutao.together_backend.entity.User;
import com.cn.yutao.together_backend.entity.dto.CreateUserDTO;
import com.cn.yutao.together_backend.entity.dto.LoginDTO;
import com.cn.yutao.together_backend.service.UserService;
import com.cn.yutao.together_backend.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody @Valid CreateUserDTO createUserDTO) {
        User user = new User();
        BeanUtils.copyProperties(createUserDTO, user);

        return userService.createUser(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody @Valid LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken token
                = UsernamePasswordAuthenticationToken.unauthenticated(loginDTO.getUsername(), loginDTO.getPassword());
        return userService.login(token);
    }

    @PostMapping("/{id}/binding/{identifyCode}")
    public User binding(@PathVariable Long id, @PathVariable String identifyCode) {
        User user = userService.fetchByIdentifyCode(identifyCode);
        userService.bind(user);
        return userService.fetchById(id);
    }

    @GetMapping("/refreshing")
    public User refreshing() {
        final var loginUser = SecurityUtils.getLoginUser();
        return userService.refresh(loginUser);
    }

}
