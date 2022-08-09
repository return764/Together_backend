package com.cn.yutao.together_backend.repository;

import com.cn.yutao.together_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdentifyCode(String code);

    Optional<User> findByUsername(String username);
}
