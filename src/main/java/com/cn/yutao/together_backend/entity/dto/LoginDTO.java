package com.cn.yutao.together_backend.entity.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDTO {
    @NotBlank(message = "username must be not blank")
    private String username;
    @NotBlank(message = "password must be not blank")
    private String password;
}
