package com.cn.yutao.together_backend.repository;

import com.cn.yutao.together_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
