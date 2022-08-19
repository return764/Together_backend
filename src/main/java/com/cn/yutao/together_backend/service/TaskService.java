package com.cn.yutao.together_backend.service;

import com.cn.yutao.together_backend.entity.Task;
import com.cn.yutao.together_backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> fetchTasks(Integer status, Long sourceUserId) {
        return taskRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("sourceUser").get("id"), sourceUserId));
            if (Objects.nonNull(status)) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
        });
    }

    public Task create(Task task) {
        taskRepository.save(task);
        return task;
    }
}
