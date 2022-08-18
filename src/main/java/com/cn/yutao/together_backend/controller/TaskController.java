package com.cn.yutao.together_backend.controller;

import com.cn.yutao.together_backend.entity.Task;
import com.cn.yutao.together_backend.entity.User;
import com.cn.yutao.together_backend.entity.dto.CreateTaskDTO;
import com.cn.yutao.together_backend.service.TaskService;
import com.cn.yutao.together_backend.service.UserService;
import com.cn.yutao.together_backend.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    @GetMapping
    public List<Task> list(@RequestParam(required = false) Integer status) {
        return taskService.fetchTasks(status);
    }

    @PostMapping
    public Task create(@RequestBody CreateTaskDTO createTaskDTO) {
        Task task = new Task();
        task.setName(createTaskDTO.getName());
        task.setDescription(createTaskDTO.getDescription());
        task.setSourceUser(SecurityUtils.getLoginUser());
        task.setTargetUser(userService.fetchById(createTaskDTO.getTargetId()));
        return taskService.create(task);
    }
}
