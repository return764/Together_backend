package com.cn.yutao.together_backend.controller;


import cn.hutool.core.util.IdUtil;
import com.cn.yutao.together_backend.entity.User;
import com.cn.yutao.together_backend.entity.dto.CreateUserDTO;
import com.cn.yutao.together_backend.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User register(@RequestBody @Valid CreateUserDTO createUserDTO) {
        User user = new User();
        BeanUtils.copyProperties(createUserDTO, user);

        return userService.createUser(user);
    }

}
