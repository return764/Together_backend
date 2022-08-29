package com.cn.yutao.together_backend.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTaskDTO {
    @NotNull(message = "任务名不能为空")
    private String name;
    @NotNull(message = "任务描述不能为空")
    private String description;
    @NotNull(message = "任务目标用户id不能为空")
    private Long targetId;
    @NotNull(message = "截止时间不能为空")
    @Future(message = "截止时间必须大于当前时间")
    private LocalDateTime deadline;
}
