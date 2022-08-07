package com.cn.yutao.together_backend.entity.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class CreateUserDTO {
    @NotBlank(message = "username must be not blank")
    @Length(min = 2, max = 20, message = "username must in length range")
    private String username;
    @NotBlank(message = "password must be not blank")
    @Length(min = 8, max = 16, message = "password must in length range")
    private String password;
    @NotBlank(message = "nickname must be not blank")
    private String nickname;
}
