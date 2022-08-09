package com.cn.yutao.together_backend.integration.security;

import com.cn.yutao.together_backend.exception.UserNotFoundException;
import com.cn.yutao.together_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@ConditionalOnProperty(name = "spring.profiles.active", havingValue = "dev", matchIfMissing = true)
public class AuthUserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final var foundUserOptional = userRepository.findByUsername(username);
        final var user = foundUserOptional.orElseThrow(() -> new UserNotFoundException("User isn't existing"));
        return User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("USER")
                .build();
    }
}
