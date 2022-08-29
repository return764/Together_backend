package com.cn.yutao.together_backend;

import com.cn.yutao.together_backend.entity.User;
import com.cn.yutao.together_backend.repository.TaskRepository;
import com.cn.yutao.together_backend.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BasicSpringBootTest {
    protected User userInDatabase;
    protected User userInDatabase2;
    protected User userInDatabase3;
    protected User userInDatabase4;
    protected User userInDatabase2WithoutPassword;
    protected String originPwd;
    @Autowired
    UserService userService;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TestRestTemplate restTemplate;
    TestRestTemplate restTemplateWithLogin;

    @BeforeAll
    void setUp() {
        originPwd = "testPassword";
        userInDatabase = User.builder()
                .username("tUname")
                .password(originPwd)
                .nickname("tNname")
                .build();
        userInDatabase2 = User.builder()
                .username("tUname2")
                .password(originPwd)
                .nickname("tNname2")
                .build();
        userInDatabase3 = User.builder()
                .username("tUname3")
                .password(originPwd)
                .nickname("tNname3")
                .build();
        userInDatabase4 = User.builder()
                .username("tUname4")
                .password(originPwd)
                .nickname("tNname4")
                .binding(userInDatabase3)
                .build();
        userService.createUser(userInDatabase);
        userService.createUser(userInDatabase2);
        userService.createUser(userInDatabase3);
        userService.createUser(userInDatabase4);

        userInDatabase2WithoutPassword = new User();
        BeanUtils.copyProperties(userInDatabase2, userInDatabase2WithoutPassword);
        userInDatabase2WithoutPassword.setPassword(null);
        this.restTemplateWithLogin = restTemplate
                .withBasicAuth(userInDatabase.getUsername(), originPwd);
    }
}
