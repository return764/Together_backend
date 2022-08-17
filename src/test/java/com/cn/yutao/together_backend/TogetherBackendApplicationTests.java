package com.cn.yutao.together_backend;

import com.cn.yutao.together_backend.entity.Task;
import com.cn.yutao.together_backend.entity.User;
import com.cn.yutao.together_backend.entity.dto.CreateUserDTO;
import com.cn.yutao.together_backend.entity.dto.LoginDTO;
import com.cn.yutao.together_backend.entity.enums.TaskStatus;
import com.cn.yutao.together_backend.exception.ErrorResult;
import com.cn.yutao.together_backend.repository.TaskRepository;
import com.cn.yutao.together_backend.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TogetherBackendApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserService userService;
    private User userInDatabase;
    private User userInDatabase2;
    private User userInDatabase2WithoutPassword;
    private String originPassword;

    @BeforeAll
    void setUp() {
        originPassword = "testPassword";
        userInDatabase = User.builder()
                .username("testUsername")
                .password(originPassword)
                .nickname("testNickname")
                .build();
        userInDatabase2 = User.builder()
                .username("testUsername2")
                .password(originPassword)
                .nickname("testNickname2")
                .build();
        userService.createUser(userInDatabase);
        userService.createUser(userInDatabase2);
        userInDatabase2WithoutPassword = new User();
        BeanUtils.copyProperties(userInDatabase2, userInDatabase2WithoutPassword);
        userInDatabase2WithoutPassword.setPassword(null);
    }


    @Nested
    class HelloWorldTest {
        @Test
        void should_hello_world() {
            final var responseEntity =
                    restTemplate
                            .withBasicAuth(userInDatabase.getUsername(), originPassword)
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
            assertThat(Objects.requireNonNull(responseEntity.getBody()).getMessage()).contains("username must be not blank");
        }

        @Test
        void should_login_successfully() {
            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setUsername(userInDatabase.getUsername());
            loginDTO.setPassword(originPassword);
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
            final var responseEntity = restTemplate
                    .withBasicAuth(userInDatabase.getUsername(), originPassword)
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
            final var responseEntity = restTemplate
                    .withBasicAuth(userInDatabase.getUsername(), originPassword)
                    .postForEntity("/users/{id}/binding/{identifyCode}",
                            null,
                            ErrorResult.class,
                            userInDatabase.getId(),
                            "XXXXXX");
            // then
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(responseEntity.getBody().getMessage()).isEqualTo("User not found.");
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class TaskTest {

        @Autowired
        TaskRepository taskRepository;

        @BeforeAll
        void setUp() {
            taskRepository.save(new Task("testname", "testdescription", 0));
            taskRepository.save(new Task("testname", "testdescription", 1));
            taskRepository.save(new Task("testname", "testdescription", 2));
        }

        @Test
        void should_list_tasks() {
            // given
            // when
            final var responseEntity = restTemplate.withBasicAuth(userInDatabase.getUsername(), originPassword)
                    .getForEntity("/tasks", Task[].class);
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
            final var responseEntity = restTemplate.withBasicAuth(userInDatabase.getUsername(), originPassword)
                    .getForEntity("/tasks?status={status}", Task[].class, map);
            // then
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            final var result = responseEntity.getBody();
            assertThat(result).hasSize(1);
            assertThat(result[0].getStatus()).isEqualTo(1);
        }
    }
}

