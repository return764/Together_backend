package com.cn.yutao.together_backend.controller;

import com.cn.yutao.together_backend.entity.User;
import com.cn.yutao.together_backend.service.UserService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureJsonTesters
public class UserControllerTest {

    @MockBean
    UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JacksonTester<User> userJson;


    @Nested
    class CreateUserTest {
        @Test
        void should_create_user_correctly() throws Exception {
            final var createdUser = User.builder()
                    .username("testUser")
                    .password("testPassword")
                    .nickname("testName")
                    .build();
            when(userService.createUser(createdUser)).thenReturn(createdUser);

            final var response = mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userJson.write(createdUser).getJson())
                    )
                    .andReturn()
                    .getResponse();

            assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
            verify(userService).createUser(createdUser);
        }
    }
}
