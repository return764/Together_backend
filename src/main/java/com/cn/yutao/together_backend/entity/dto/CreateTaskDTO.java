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
    @NotNull(message = "Task name should not be null.")
    private String name;
    @NotNull(message = "Task description should not be null.")
    private String description;
    @NotNull(message = "targetId should not be null.")
    private Long targetId;
    @NotNull(message = "deadline should not be null.")
    @Future(message = "deadline must in future.")
    private LocalDateTime deadline;
}
