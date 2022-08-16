package com.cn.yutao.together_backend.integration.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserAuthentication extends User {

    private com.cn.yutao.together_backend.entity.User loginUser;

    public UserAuthentication(String username, String password, String... authorities) {
        super(username, password, AuthorityUtils.createAuthorityList(authorities));
    }

    public UserAuthentication(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public UserAuthentication(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public com.cn.yutao.together_backend.entity.User getLoginUser() {
        return loginUser;
    }

    public UserAuthentication withLoginUser(com.cn.yutao.together_backend.entity.User loginUser) {
        this.loginUser = loginUser;
        return this;
    }

}
