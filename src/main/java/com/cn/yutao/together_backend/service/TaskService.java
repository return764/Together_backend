package com.cn.yutao.together_backend.service;

import com.cn.yutao.together_backend.entity.Task;
import com.cn.yutao.together_backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task create(Task task) {
        taskRepository.save(task);
        return task;
    }

    public List<Task> fetchTasks(Task task) {
        return taskRepository.findAll(Example.of(task));
    }
}
