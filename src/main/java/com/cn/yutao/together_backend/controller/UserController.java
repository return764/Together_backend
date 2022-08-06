package com.cn.yutao.together_backend.controller;


import com.cn.yutao.together_backend.entity.User;
import com.cn.yutao.together_backend.entity.dto.CreateUserDTO;
import com.cn.yutao.together_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public User register(@RequestBody CreateUserDTO createUserDTO) {
        User user = new User();
        user.setUsername(createUserDTO.getUsername());
        user.setPassword(createUserDTO.getPassword());
        user.setNickname(createUserDTO.getNickname());

        return userRepository.save(user);
    }
}
