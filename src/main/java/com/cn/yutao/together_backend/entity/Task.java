package com.cn.yutao.together_backend.entity;

import com.cn.yutao.together_backend.entity.enums.TaskStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity(name = "tasks")
public class Task {

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task(String name, String description, Integer status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, Integer status, User sourceUser, User targetUser, LocalDateTime deadline) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.sourceUser = sourceUser;
        this.targetUser = targetUser;
        this.deadline = deadline;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "source_user_id")
    private User sourceUser;

    @ManyToOne
    @JoinColumn(name = "target_user_id")
    private User targetUser;

    private LocalDateTime deadline;
    private LocalDateTime createAt;

    @PrePersist
    void prePersist() {
        if (Objects.isNull(status)) {
            status = TaskStatus.UNCOMPLETED.value();
        }
        if (Objects.isNull(createAt)) {
            createAt = LocalDateTime.now();
        }
    }
}
