package com.cn.yutao.together_backend.controller;

import com.cn.yutao.together_backend.entity.Task;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @GetMapping
    public List<Task> list() {
        return List.of(new Task("task1", "test"));
    }
 }
