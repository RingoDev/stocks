package com.ringodev.stocks.service.user;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

import static javax.persistence.GenerationType.AUTO;

@Entity
public class AuthorityImpl implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = AUTO)
    long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(unique = true, nullable = false)
    String role;

    public AuthorityImpl(String role) {
        this.role = role;
    }

    public AuthorityImpl() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }

    public static AuthorityImpl of(GrantedAuthority authority) {
        return new AuthorityImpl(authority.getAuthority());
    }

    public void setAuthority(String auth) {
        this.role = auth;
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
        return role.equals(authority.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role);
    }


}
