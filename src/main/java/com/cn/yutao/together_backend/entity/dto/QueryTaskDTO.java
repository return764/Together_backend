package com.cn.yutao.together_backend.entity.dto;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class QueryTaskDTO {
    private final Integer status;
    private final Long sourceUserId;
}
