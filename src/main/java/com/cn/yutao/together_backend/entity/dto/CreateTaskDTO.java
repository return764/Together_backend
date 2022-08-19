package com.cn.yutao.together_backend.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateTaskDTO {
    private String name;
    private String description;
    private Long targetId;
    private LocalDateTime deadline;
}
