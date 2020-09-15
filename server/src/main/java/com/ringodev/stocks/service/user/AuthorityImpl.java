package com.ringodev.stocks.service.user;

import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

public class AuthorityImpl implements GrantedAuthority {

    Role role;

    public AuthorityImpl(Role role){
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorityImpl authority = (AuthorityImpl) o;
        return role == authority.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(role);
    }
}
