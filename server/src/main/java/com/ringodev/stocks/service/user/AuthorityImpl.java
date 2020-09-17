package com.ringodev.stocks.service.user;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Enumerated;
import java.util.Objects;

import static javax.persistence.EnumType.STRING;

public class AuthorityImpl implements GrantedAuthority {

    @Enumerated(STRING)
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
