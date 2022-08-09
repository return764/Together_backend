package com.cn.yutao.together_backend.service;

import com.cn.yutao.together_backend.entity.User;
import com.cn.yutao.together_backend.repository.UserRepository;
import com.cn.yutao.together_backend.utils.IdentifyCodeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserService userService;

    private User user;
    private User userStored;
    private User secondUserStored;
    private String firstCode;
    private String secondCode;

    @BeforeAll
    void beforeAll() {
        Mockito.mockStatic(IdentifyCodeUtils.class);
        firstCode = "111111";
        secondCode = "222222";
    }

    @BeforeEach
    void beforeEach() {
        // given
        final var commonUserBuilder = User.builder()
                .nickname("testName")
                .username("testName")
                .password("testPassword");
        user = commonUserBuilder
                .build();
        userStored = commonUserBuilder
                .identifyCode(firstCode)
                .build();
        secondUserStored = commonUserBuilder
                .identifyCode(secondCode)
                .build();
        when(passwordEncoder.encode(user.getPassword())).thenReturn(user.getPassword());
    }

    @Test
    void should_create_user_correctly() {
        // given
        when(repository.save(user)).thenReturn(userStored);
        Mockito.when(IdentifyCodeUtils.genIdCode()).thenReturn(firstCode);
        // when
        final var savedUser = userService.createUser(user);
        // then
        assertThat(savedUser).isEqualTo(userStored);
        verify(repository).save(user);
    }

    @Test
    void should_remake_identify_code_when_generate_same_code() {
        // given
        when(repository.findByIdentifyCode(firstCode)).thenReturn(Optional.of(userStored));
        when(repository.findByIdentifyCode(secondCode)).thenReturn(Optional.empty());
        when(repository.save(user)).thenReturn(secondUserStored);
        Mockito.when(IdentifyCodeUtils.genIdCode())
                .thenReturn(firstCode)
                .thenReturn(secondCode);
        // when
        final var newStoredUser = userService.createUser(user);
        // then
        assertThat(newStoredUser.getIdentifyCode()).isEqualTo(secondCode);
        verify(repository).findByIdentifyCode(firstCode);
        verify(repository).findByIdentifyCode(secondCode);
        verify(repository, times(1)).save(user);
    }
}
