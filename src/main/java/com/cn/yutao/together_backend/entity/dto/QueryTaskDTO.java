package com.cn.yutao.together_backend.entity.dto;

public class QueryTaskDTO {
    private final Integer status;
    private final Long sourceUserId;

    public QueryTaskDTO(Integer status, Long sourceUserId) {
        this.status = status;
        this.sourceUserId = sourceUserId;
    }

    public Integer getStatus() {
        return status;
    }

    public Long getSourceUserId() {
        return sourceUserId;
    }
}
