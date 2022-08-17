package com.cn.yutao.together_backend.entity;

import com.cn.yutao.together_backend.entity.enums.TaskStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity(name = "tasks")
public class Task {

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Integer status;

    @PrePersist
    void prePersist() {
        if (Objects.isNull(status)) {
            status = TaskStatus.UNCOMPLETED.value();
        }
    }
}
