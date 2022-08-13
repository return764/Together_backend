package com.cn.yutao.together_backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.util.Objects;

@Data
@Builder
@Entity(name = "users")
@DynamicUpdate
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = @Index(name = "id_username", columnList = "username", unique = true))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private String nickname;
    private Long point;
    private String identifyCode;

    @OneToOne
    @JoinColumn(name = "binding_user_id")
    private User binding;

    public User getBinding() {
        if (Objects.isNull(binding)) {
            return null;
        }
        if (Objects.nonNull(binding.binding)) {
            binding.binding = null;
        }
        return binding;
    }

    @PrePersist
    void preInsert() {
        if (Objects.isNull(point)) {
            point = 0L;
        }
    }
}
