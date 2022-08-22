package com.cn.yutao.together_backend.service;

import com.cn.yutao.together_backend.entity.Task;
import com.cn.yutao.together_backend.exception.TaskNotFoundException;
import com.cn.yutao.together_backend.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    TaskRepository repository;
    @InjectMocks
    TaskService taskService;
    private List<Task> tasks;
    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task("test", "test", 0);
        tasks = List.of(task,
                new Task("test", "test", 1),
                new Task("test", "test", 2));
    }

    @Test
    void should_return_task_fetch_by_id() {
        // given
        when(repository.findById(1L)).thenReturn(Optional.of(task));
        // when
        final var result = taskService.fetchTaskById(1L);
        // then
        assertThat(result).isEqualTo(task);
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void should_throw_task_not_found_exception_when_fetch_null_task() {
        // given
        when(repository.findById(1L)).thenReturn(Optional.empty());
        // when
        // then
        assertThrows(TaskNotFoundException.class, () -> taskService.fetchTaskById(1L));
    }
}
