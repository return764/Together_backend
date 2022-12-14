package com.cn.yutao.together_backend.controller;

import com.cn.yutao.together_backend.entity.Task;
import com.cn.yutao.together_backend.entity.dto.CreateTaskDTO;
import com.cn.yutao.together_backend.entity.dto.QueryTaskDTO;
import com.cn.yutao.together_backend.entity.dto.UpdateTaskDTO;
import com.cn.yutao.together_backend.service.TaskService;
import com.cn.yutao.together_backend.service.UserService;
import com.cn.yutao.together_backend.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    @GetMapping
    public List<Task> list(QueryTaskDTO queryTaskDTO) {
        Task task = new Task();
        task.setStatus(queryTaskDTO.getStatus());
        task.setSourceUser(userService.fetchById(queryTaskDTO.getSourceUserId()));
        return taskService.fetchTasks(task);
    }

    @PostMapping
    public Task create(@RequestBody @Valid CreateTaskDTO createTaskDTO) {
        Task task = new Task();
        task.setName(createTaskDTO.getName());
        task.setDescription(createTaskDTO.getDescription());
        task.setSourceUser(SecurityUtils.getLoginUser());
        task.setTargetUser(userService.fetchById(createTaskDTO.getTargetId()));
        task.setDeadline(createTaskDTO.getDeadline());
        task.setPoint(createTaskDTO.getPoint());
        return taskService.createOrUpdate(task);
    }

    @PutMapping("/{id}")
    public Task updateStatus(@PathVariable Long id, @RequestBody @Valid UpdateTaskDTO taskDTO) {
        final var task = taskService.fetchTaskById(id);
        task.setStatus(taskDTO.getStatus());
        return taskService.createOrUpdate(task);
    }
}
