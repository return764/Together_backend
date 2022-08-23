package com.cn.yutao.together_backend.controller;

import com.cn.yutao.together_backend.entity.Task;
import com.cn.yutao.together_backend.entity.User;
import com.cn.yutao.together_backend.entity.dto.CreateTaskDTO;
import com.cn.yutao.together_backend.entity.dto.UpdateTaskDTO;
import com.cn.yutao.together_backend.service.TaskService;
import com.cn.yutao.together_backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {

    @MockBean
    TaskService taskService;
    @MockBean
    UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper om;

    @MethodSource("createTaskProvider")
    @ParameterizedTest
    void should_create_failed_when_given_null_task_name(CreateTaskDTO createTaskDTO) throws Exception {
        // given
        when(taskService.createOrUpdate(any())).thenReturn(new Task());
        when(userService.fetchById(2L)).thenReturn(new User());
        // when
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(createTaskDTO)))
                .andExpect(status().isBadRequest());
        // then
        verify(taskService, never()).createOrUpdate(any());
    }

    static Stream<CreateTaskDTO> createTaskProvider() {
        return Stream.of(
                new CreateTaskDTO(null, "test", 2L, LocalDateTime.now().plusDays(1)),
                new CreateTaskDTO("test", null, 2L, LocalDateTime.now().plusDays(1)),
                new CreateTaskDTO("test", "test", null, LocalDateTime.now().plusDays(1)),
                new CreateTaskDTO("test", "test", 2L, null),
                new CreateTaskDTO("test", "test", 2L, LocalDateTime.now().minusDays(1))
        );
    }

    @Test
    void should_complete_task_failed_when_given_unsupport_status_value() throws Exception {
        // given
        when(taskService.fetchTaskById(1L)).thenReturn(new Task());
        final var updateTaskDTO = new UpdateTaskDTO();
        updateTaskDTO.setStatus(-1);
        // when
        final var response = mockMvc.perform(put("/tasks/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(updateTaskDTO)))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        verify(taskService, never()).fetchTaskById(1L);
    }
}
