package com.cn.yutao.together_backend.integration.security;

import com.cn.yutao.together_backend.exception.UserNotFoundException;
import com.cn.yutao.together_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class AuthUserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final var foundUserOptional = userRepository.findByUsername(username);
        final var user = foundUserOptional.orElseThrow(() -> new UserNotFoundException("User isn't existing"));
        return new UserAuthentication(user.getUsername(), user.getPassword(), "USER").withLoginUser(user);
    }
}
