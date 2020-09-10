package com.ringodev.stocks.service.user;

import org.springframework.security.core.GrantedAuthority;

public class AuthorityImpl implements GrantedAuthority {

    Role role;

    AuthorityImpl(Role role){
        this.role = role;
    }


    @Override
    public String getAuthority() {
        return role.toString();
    }

    @Override
    public String toString() {
        return "AuthorityImpl{" +
                "role=" + role +
                '}';
    }
}
