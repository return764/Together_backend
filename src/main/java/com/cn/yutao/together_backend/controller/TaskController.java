package com.cn.yutao.together_backend.controller;

import com.cn.yutao.together_backend.entity.Task;
import com.cn.yutao.together_backend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public List<Task> list(@RequestParam(required = false) Integer status) {
        return taskService.fetchTasks(status);
    }
}
