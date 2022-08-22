package com.cn.yutao.together_backend.service;

import com.cn.yutao.together_backend.entity.Task;
import com.cn.yutao.together_backend.exception.TaskNotFoundException;
import com.cn.yutao.together_backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task createOrUpdate(Task task) {
        taskRepository.save(task);
        return task;
    }

    public List<Task> fetchTasks(Task task) {
        return taskRepository.findAll(Example.of(task));
    }

    public Task fetchTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found."));
    }

}
