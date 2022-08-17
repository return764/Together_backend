package com.cn.yutao.together_backend.service;

import com.cn.yutao.together_backend.entity.Task;
import com.cn.yutao.together_backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> fetchTasks() {
        return taskRepository.findAll();
    }

    public List<Task> fetchTasks(Integer status) {
        if (Objects.isNull(status)) {
            return fetchTasks();
        }
        return taskRepository.findAll((root, query, cb) -> cb.equal(root.get("status"), status));
    }
}
