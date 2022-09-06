package com.cn.yutao.together_backend.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @NotNull(message = "任务积分不能为空")
    private Integer point;
    @NotNull(message = "截止时间不能为空")
    @Future(message = "截止时间必须大于当前时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;
}
