package com.cn.yutao.together_backend;

import com.cn.yutao.together_backend.entity.Task;
import com.cn.yutao.together_backend.entity.User;
import com.cn.yutao.together_backend.entity.dto.CreateTaskDTO;
import com.cn.yutao.together_backend.entity.dto.CreateUserDTO;
import com.cn.yutao.together_backend.entity.dto.LoginDTO;
import com.cn.yutao.together_backend.entity.dto.UpdateTaskDTO;
import com.cn.yutao.together_backend.entity.enums.TaskStatus;
import com.cn.yutao.together_backend.exception.ErrorResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;


class TogetherBackendApplicationTests extends BasicSpringBootTest {


    @Nested
    class HelloWorldTest {
        @Test
        void should_hello_world() {
            final var responseEntity =
                    restTemplateWithLogin
                            .getForEntity("/hello", String.class);

            assertThat(responseEntity.getBody()).isEqualTo("World");
        }

    }

    @Nested
    class UserTest {

        CreateUserDTO createdUser;

        @BeforeEach
        public void beforeEach() {
            createdUser = CreateUserDTO.builder()
                    .username("testUser")
                    .password("testPassword")
                    .nickname("testName")
                    .build();
        }

        @Test
        void should_register_user() {
            final var responseEntity = restTemplate.postForEntity("/users", createdUser, User.class);

            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            final var user = responseEntity.getBody();
            assertThat(user).isNotNull();
            assertThat(user.getUsername()).isEqualTo(createdUser.getUsername());
            assertThat(user.getNickname()).isEqualTo(createdUser.getNickname());
            assertThat(user.getIdentifyCode()).isNotNull();
            assertThat(user.getPoint()).isZero();
        }

        @Test
        void should_register_failed_when_given_incorrect_params() {
            final var failedCreateUser = CreateUserDTO.builder()
                    .nickname("testnickname")
                    .password("testpassword")
                    .username("")
                    .build();
            final var responseEntity = restTemplate.postForEntity("/users", failedCreateUser, ErrorResult.class);

            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
            assertThat(Objects.requireNonNull(responseEntity.getBody()).getMessage()).contains("用户名不能为空");
        }

        @Test
        void should_login_successfully() {
            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setUsername(userInDatabase.getUsername());
            loginDTO.setPassword(originPwd);
            final var responseEntity = restTemplate.postForEntity("/users/login", loginDTO, User.class);

            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            final var user = responseEntity.getBody();
            assertThat(user).isNotNull();
        }

        @Test
        void should_login_failed() {
            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setUsername(userInDatabase.getUsername());
            loginDTO.setPassword("failedPassword");
            final var responseEntity = restTemplate.postForEntity("/users/login", loginDTO, ErrorResult.class);

            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }

        @Test
        void should_bind_user_successfully() {
            // given
            User result = User.builder()
                    .nickname(userInDatabase.getNickname())
                    .username(userInDatabase.getUsername())
                    .identifyCode(userInDatabase.getIdentifyCode())
                    .id(userInDatabase.getId())
                    .point(userInDatabase.getPoint())
                    .binding(userInDatabase2WithoutPassword)
                    .build();

            // when
            final var responseEntity = restTemplateWithLogin
                    .postForEntity("/users/{id}/binding/{identifyCode}",
                            null,
                            User.class,
                            userInDatabase.getId(),
                            userInDatabase2.getIdentifyCode());

            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(responseEntity.getBody()).isEqualTo(result);
        }

        @Test
        void should_bind_user_failed() {
            // given

            // when
            final var responseEntity = restTemplateWithLogin
                    .postForEntity("/users/{id}/binding/{identifyCode}",
                            null,
                            ErrorResult.class,
                            userInDatabase.getId(),
                            "XXXXXX");
            // then
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(responseEntity.getBody().getMessage()).isEqualTo("User not found.");
        }

        @Test
        void should_bind_failed_when_bind_self() {
            // given
            // when
            final var responseEntity = restTemplateWithLogin
                    .postForEntity("/users/{id}/binding/{identifyCode}",
                            null,
                            ErrorResult.class,
                            userInDatabase.getId(),
                            userInDatabase.getIdentifyCode());
            // then
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(responseEntity.getBody().getMessage()).contains("绑定用户失败：不能绑定自己");
        }

        @Test
        void should_bind_failed_when_bind_some_one_already_bind() {
            // given
            // when
            final var responseEntity = restTemplateWithLogin
                    .postForEntity("/users/{id}/binding/{identifyCode}",
                            null,
                            ErrorResult.class,
                            userInDatabase.getId(),
                            userInDatabase4.getIdentifyCode());
            // then
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(responseEntity.getBody().getMessage()).contains("绑定用户失败：用户已被绑定");
        }

        @Test
        void should_refresh_user() {
            // given
            // when
            final var responseEntity = restTemplateWithLogin
                    .getForEntity("/users/refreshing",
                            User.class);
            // then
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            final var body = responseEntity.getBody();
            assertThat(body.getId()).isEqualTo(userInDatabase.getId());
        }
    }

    @Nested
    class TaskTest {

        private Task user2Task;

        @BeforeEach
        void setUp() {
            user2Task = new Task("testname", "testdescription", 0, userInDatabase2, userInDatabase, LocalDateTime.now().plusDays(1));
            taskRepository.save(new Task("testname", "testdescription", 0, userInDatabase, userInDatabase2, LocalDateTime.now().plusDays(1)));
            taskRepository.save(new Task("testname", "testdescription", 1, userInDatabase, userInDatabase2, LocalDateTime.now().plusDays(1)));
            taskRepository.save(new Task("testname", "testdescription", 2, userInDatabase, userInDatabase2, LocalDateTime.now().plusDays(1)));
            taskRepository.save(user2Task);
        }

        @AfterEach
        void tearDown() {
            taskRepository.deleteAll();
        }

        @Test
        void should_list_tasks_with_source_user_filter() {
            // given
            final var map = new HashMap<String, String>();
            map.put("sourceUserId", userInDatabase.getId().toString());
            // when
            final var responseEntity = restTemplateWithLogin
                    .getForEntity("/tasks?sourceUserId={sourceUserId}", Task[].class, map);
            // then
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

            final var body = responseEntity.getBody();
            assertThat(body).hasSize(3);
        }

        @Test
        void should_list_tasks_with_status_filter() {
            // given
            // when
            final var map = new HashMap<String, String>();
            map.put("status", "1");
            map.put("sourceUserId", userInDatabase.getId().toString());
            final var responseEntity = restTemplateWithLogin
                    .getForEntity("/tasks?status={status}&sourceUserId={sourceUserId}", Task[].class, map);
            // then
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            final var result = responseEntity.getBody();
            assertThat(result).hasSize(1);
            assertThat(result[0].getStatus()).isEqualTo(1);
        }

        @Test
        void should_create_task_when_given_name_and_desc_and_target() {
            // given
            final var now = LocalDateTime.now();
            CreateTaskDTO createTaskDTO = new CreateTaskDTO();
            createTaskDTO.setName("test");
            createTaskDTO.setDescription("test");
            createTaskDTO.setTargetId(userInDatabase2.getId());
            createTaskDTO.setDeadline(now.plusDays(1));
            // when
            final var responseEntity = restTemplateWithLogin
                    .postForEntity("/tasks", createTaskDTO, Task.class);
            // then
            final var savedTask = responseEntity.getBody();
            assertThat(savedTask.getName()).isEqualTo(createTaskDTO.getName());
            assertThat(savedTask.getDescription()).isEqualTo(createTaskDTO.getDescription());
            assertThat(savedTask.getStatus()).isEqualTo(TaskStatus.UNCOMPLETED.value());
            assertThat(savedTask.getSourceUser().getId()).isEqualTo(userInDatabase.getId());
            assertThat(savedTask.getTargetUser().getId()).isEqualTo(userInDatabase2.getId());
            assertThat(savedTask.getDeadline()).isEqualTo(createTaskDTO.getDeadline());
            assertThat(savedTask.getCreateAt()).isCloseTo(now, within(1, ChronoUnit.SECONDS));
        }

        @EnumSource(value = TaskStatus.class)
        @ParameterizedTest
        void should_change_task_state_when_update_task(TaskStatus status) {
            // given
            UpdateTaskDTO task = new UpdateTaskDTO();
            task.setStatus(status.value());
            // when
            final var response = restTemplateWithLogin.exchange(
                    "/tasks/{id}",
                    HttpMethod.PUT,
                    new HttpEntity<>(task),
                    Task.class,
                    user2Task.getId()
            );
            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().getStatus()).isEqualTo(status.value());
        }
    }
}

