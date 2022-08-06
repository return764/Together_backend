package com.cn.yutao.together_backend;

import com.cn.yutao.together_backend.entity.User;
import com.cn.yutao.together_backend.entity.dto.CreateUserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TogetherBackendApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;

    CreateUserDTO createdUser;

    @Nested
    class HelloWorldTest {
        @Test
        void should_hello_world() {
            final var responseEntity = restTemplate.getForEntity("/hello", String.class);

            assertThat(responseEntity.getBody()).isEqualTo("World");
        }

    }

    @Nested
    class UserTest {

        @BeforeEach
        public void beforeEach() {
            createdUser = CreateUserDTO.builder()
                    .username("testUser")
                    .password("testPassword")
                    .build();
        }

        @Test
        void should_register_user() {
            final var responseEntity = restTemplate.postForEntity("/users/register", createdUser, User.class);

            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            final var user = responseEntity.getBody();
            assertThat(user).isNotNull();
            assertThat(user.getUsername()).isEqualTo(createdUser.getUsername());
            assertThat(user.getPassword()).isEqualTo(createdUser.getPassword());
        }
    }

    
}