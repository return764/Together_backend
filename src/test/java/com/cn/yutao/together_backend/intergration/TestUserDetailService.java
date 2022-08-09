package com.cn.yutao.together_backend.intergration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;


@ConditionalOnProperty(name = "spring.profiles.active", havingValue = "test")
@Component
public class TestUserDetailService implements UserDetailsService {

    InMemoryUserDetailsManager memoryUserDetailsManager;

    public TestUserDetailService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("root")
                .password("{bcrypt}$2a$10$KAR9lsUiJaUe32HyBJftyOOEFh47uzVHNDZ7ZTb/znmGnoX4RsCc6")
                .roles("USER")
                .build());
        memoryUserDetailsManager = manager;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memoryUserDetailsManager.loadUserByUsername(username);
    }
}
