package com.cn.yutao.together_backend.repository;


import com.cn.yutao.together_backend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    TestEntityManager entityManager;
    @Autowired
    UserRepository userRepository;

    @Test
    void should_return_user_when_identify_code_exist() {
        // given
        final var idCode = "AX2G3E";
        final var user = User.builder()
                .nickname("testUsername")
                .username("testUsername")
                .password("testPassword")
                .identifyCode(idCode).build();

        entityManager.persist(user);
        // when
        final var foundUserOpt = userRepository.findByIdentifyCode(idCode);

        // then
        assertThat(foundUserOpt).isPresent().contains(user);
        final var foundUser = foundUserOpt.get();
        assertThat(foundUser.getPoint()).isZero();
    }
}
