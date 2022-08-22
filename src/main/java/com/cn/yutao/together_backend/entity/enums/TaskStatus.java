package com.cn.yutao.together_backend.entity.enums;

public enum TaskStatus {
    UNCOMPLETED(0, "uncompleted"),
    TO_EXAMINE(1, "to examine"),
    COMPLETED(2, "completed"),
    DELETED(3, "deleted");

    private final Integer status;
    private final String name;

    TaskStatus(Integer status, String name) {
        this.status = status;
        this.name = name;
    }

    public Integer value() {
        return this.status;
    }
}
