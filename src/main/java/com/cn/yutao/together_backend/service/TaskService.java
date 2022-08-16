package com.cn.yutao.together_backend.service;

import com.cn.yutao.together_backend.entity.Task;
import com.cn.yutao.together_backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> fetchTasks() {
        return taskRepository.findAll();
    }
}
