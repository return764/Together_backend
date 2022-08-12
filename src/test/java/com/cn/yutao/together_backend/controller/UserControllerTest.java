package com.cn.yutao.together_backend.controller;

import com.cn.yutao.together_backend.entity.User;
import com.cn.yutao.together_backend.entity.dto.LoginDTO;
import com.cn.yutao.together_backend.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    @Autowired
    private ObjectMapper om;


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

    @Nested
    class LoginTest {
        @Test
        void should_login_successfully() throws Exception {
            // given
            final var pwd = "testPassword";
            final var uname = "testUsername";
            LoginDTO loginDTO = new LoginDTO();
            User user = new User();
            loginDTO.setPassword(pwd);
            loginDTO.setUsername(uname);
            UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(uname, pwd);
            when(userService.login(token)).thenReturn(user);
            // when
            // then
            mockMvc.perform(post("/users/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(om.writeValueAsString(loginDTO))
                    ).andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().string(userJson.write(user).getJson()));
        }

        @Test
        void should_login_failed_when_input_no_password_params() throws Exception {
            // given
            final var pwd = "";
            final var uname = "testUsername";
            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setUsername(uname);
            loginDTO.setPassword(pwd);
            User user = new User();
            when(userService.login(any())).thenReturn(user);
            // when
            // then
            mockMvc.perform(post("/users/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(om.writeValueAsString(loginDTO))
            ).andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").value("password must be not blank"));
            verify(userService, never()).login(any());
        }
    }
}
