package com.cn.yutao.together_backend.service;

import com.cn.yutao.together_backend.entity.Task;
import com.cn.yutao.together_backend.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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

    @BeforeEach
    void setUp() {
        tasks = List.of(new Task("test", "test", 0),
                new Task("test", "test", 1),
                new Task("test", "test", 2));
    }

    @Test
    void should_return_all_tasks_when_given_null_status() {
        // given
        when(repository.findAll()).thenReturn(tasks);
        // when
        final var result = taskService.fetchTasks(null);
        // then
        assertThat(result).hasSize(3).isEqualTo(tasks);
        verify(repository, times(1)).findAll();
    }

    @Test
    void should_return_tasks_filter_by_status() {
        // given
        when(repository.findAll(any(Specification.class)))
                .thenReturn(List.of(tasks.get(1)));
        // when
        final var result = taskService.fetchTasks(1);
        // then
        assertThat(result).hasSize(1).isEqualTo(List.of(tasks.get(1)));
        verify(repository, times(1)).findAll(any(Specification.class));
    }
}
