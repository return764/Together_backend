package com.cn.yutao.together_backend.entity.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserDTO {
    private String username;
    private String password;
}
