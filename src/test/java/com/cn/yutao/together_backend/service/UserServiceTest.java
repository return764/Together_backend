package com.cn.yutao.together_backend.service;

import cn.hutool.core.util.IdUtil;
import com.cn.yutao.together_backend.entity.User;
import com.cn.yutao.together_backend.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository repository;

    @InjectMocks
    UserService userService;
    private User user;
    private User userStored;

    @BeforeEach
    void beforeEach() {
        // given
        user = User.builder()
                .nickname("testName")
                .username("testName")
                .password("testPassword")
                .build();
        userStored = User.builder()
                .nickname("testName")
                .username("testName")
                .password("testPassword")
                .identifyCode("AX2FL9")
                .build();
        when(repository.save(user)).thenReturn(userStored);
        Mockito.mockStatic(IdUtil.class).when(IdUtil::fastSimpleUUID).thenReturn("ax2fl9safw921v8");
    }

    @Test
    void should_create_user_correctly() {
        // when
        final var savedUser = userService.createUser(user);
        // then
        assertThat(savedUser).isEqualTo(userStored);
        verify(repository).save(user);
    }
}
