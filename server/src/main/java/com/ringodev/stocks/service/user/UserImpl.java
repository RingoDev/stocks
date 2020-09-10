package com.ringodev.stocks.service.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.AUTO;

@Entity
public class UserImpl {

    // id for internal calling of other Services
    @Id
    @GeneratedValue(strategy = AUTO)
    Long id;
    @Column(unique = true, nullable = false)
    String username;
    @Column(nullable = false)
    String password;
    @Column()
    String firstname;
    @Column()
    String lastname;
    @Column()
    String email;
    @Column(nullable = false)
    @Enumerated(STRING)
    Role role;
    @Column(nullable = false)
    Boolean enabled = false;
    @Column(nullable = false)
    Date dateCreate = new Date();

    public UserImpl() {
    }

    public UserImpl(String username, String password) {
        this(username, password, Role.ROLE_USER, true, new Date());

    }
    public UserImpl(String username, String password,Role role) {
        this(username, password, role, true, new Date());
    }

    public UserImpl(String username, String password, Role role, boolean enabled, Date dateCreate) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
        this.dateCreate = dateCreate;
    }

    @Override
    public String toString() {
        return "UserImpl{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", enabled=" + enabled +
                ", dateCreate=" + dateCreate +
                '}';
    }

    private List<GrantedAuthority> getAuthorities(){
        List<GrantedAuthority> list = new ArrayList<>();
        GrantedAuthority auth = new AuthorityImpl(this.role);
        list.add(auth);
        return list;
    }

    public User toUserDetails() {
        return new User(this.username, this.password, this.getAuthorities());
    }
}
