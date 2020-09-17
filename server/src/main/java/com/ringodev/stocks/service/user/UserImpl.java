package com.ringodev.stocks.service.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    GrantedAuthority auth;
    @Column(nullable = false)
    Boolean enabled = false;
    @Column(nullable = false)
    Date dateCreate = new Date();

    public UserImpl() {
    }

    public UserImpl(String username, String password) {
        this(username, password, new AuthorityImpl(Role.ROLE_USER), true, new Date());

    }

    public UserImpl(User user){
        this(user.getUsername(), user.getPassword(), user.getAuthorities().stream().findFirst().orElse(new AuthorityImpl(Role.ROLE_USER)), true, new Date());
    }
    public UserImpl(String username, String password,GrantedAuthority auth) {
        this(username, password, auth, true, new Date());
    }

    public UserImpl(String username, String password, GrantedAuthority auth, boolean enabled, Date dateCreate) {
        this.username = username;
        this.password = password;
        this.auth = auth;
        this.enabled = enabled;
        this.dateCreate = dateCreate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public GrantedAuthority getAuth() {
        return auth;
    }

    public void setAuth(GrantedAuthority auth) {
        this.auth = auth;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
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
                ", auth=" + auth +
                ", enabled=" + enabled +
                ", dateCreate=" + dateCreate +
                '}';
    }

    private List<GrantedAuthority> getAuthorities(){
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(this.auth);
        return list;
    }

    public User toUserDetails() {
        return new User(this.username, this.password, this.getAuthorities());
    }
}
