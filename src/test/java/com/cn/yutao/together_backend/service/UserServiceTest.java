package com.cn.yutao.together_backend.service;

import cn.hutool.core.util.IdUtil;
import com.cn.yutao.together_backend.entity.User;
import com.cn.yutao.together_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository repository;

    @InjectMocks
    UserService userService;
    private User user;
    private User userStored;
    private User secondUserStored;

    @BeforeAll
    void beforeAll() {
        Mockito.mockStatic(IdUtil.class);
    }

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
        secondUserStored = User.builder()
                .nickname("testName")
                .username("testName")
                .password("testPassword")
                .identifyCode("VF2DG3")
                .build();

    }

    @Test
    void should_create_user_correctly() {
        // given
        when(repository.save(user)).thenReturn(userStored);
        Mockito.when(IdUtil.fastSimpleUUID()).thenReturn("ax2fl9safw921v8");
        // when
        final var savedUser = userService.createUser(user);
        // then
        assertThat(savedUser).isEqualTo(userStored);
        verify(repository).save(user);
    }

    @Test
    void should_remake_identify_code_when_generate_same_code() {
        // given
        final var firstCode = "AX2FL9";
        final var secondCode = "VF2DG3";
        when(repository.findByIdentifyCode(firstCode)).thenReturn(Optional.of(userStored));
        when(repository.findByIdentifyCode(secondCode)).thenReturn(Optional.empty());
        when(repository.save(user)).thenReturn(secondUserStored);
        Mockito.when(IdUtil.fastSimpleUUID())
                .thenReturn("ax2fl9safw921v8")
                .thenReturn("vf2dg3cz8vb0ab3");
        // when
        final var newStoredUser = userService.createUser(user);
        // then
        assertThat(newStoredUser.getIdentifyCode()).isEqualTo(secondCode);
        verify(repository).findByIdentifyCode(firstCode);
        verify(repository).findByIdentifyCode(secondCode);
        verify(repository, times(1)).save(user);
    }
}
