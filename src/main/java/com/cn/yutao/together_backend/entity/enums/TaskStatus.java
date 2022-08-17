package com.cn.yutao.together_backend.entity.enums;

public enum TaskStatus {
    UNCOMPLETED(0, "uncompleted");

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
