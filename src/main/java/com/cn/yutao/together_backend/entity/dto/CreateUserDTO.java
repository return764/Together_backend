package com.cn.yutao.together_backend.entity.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class CreateUserDTO {
    @NotBlank(message = "用户名不能为空")
    @Length(min = 2, max = 20, message = "用户名必须在2-20位之间")
    private String username;
    @NotBlank(message = "密码不能为空")
    @Length(min = 8, max = 16, message = "密码必须在8-16位之间")
    private String password;
    @NotBlank(message = "昵称不能为空")
    private String nickname;
}
