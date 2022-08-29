package com.cn.yutao.together_backend.entity.dto;

import com.cn.yutao.together_backend.annotation.EnumValidator;
import com.cn.yutao.together_backend.entity.enums.TaskStatus;
import lombok.Data;

@Data
public class UpdateTaskDTO {
    @EnumValidator(value = TaskStatus.class, message = "请使用正确的任务状态")
    private Integer status;
}
