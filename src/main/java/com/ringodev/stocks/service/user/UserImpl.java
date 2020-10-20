package com.ringodev.stocks.service.user;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.SortNatural;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.GenerationType.AUTO;

@Entity
public class UserImpl implements UserDetails {

    private static final Log logger = LogFactory.getLog(UserImpl.class);

    // id for internal calling of other Services
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @SortNatural
    private Set<AuthorityImpl> authoritiesImpl;
    @Column(nullable = false)
    private Boolean enabled = false;
    @Column(nullable = false)
    private boolean accountNonExpired;
    @Column(nullable = false)
    private boolean accountNonLocked;
    @Column(nullable = false)
    private boolean credentialsNonExpired;


    public UserImpl() {

    }

    public UserImpl(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this(username, password, authorities, true, true, true, true);
    }

    public UserImpl(String username, String password, Collection<? extends GrantedAuthority> grantedAuthorities, boolean enabled, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired) {
        this.username = username;
        this.password = password;
        this.authoritiesImpl = convertAndSortAuthorities(grantedAuthorities);
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public static UserImpl from(UserDetails userDetails) {
        return new UserImpl(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities(), userDetails.isEnabled(), userDetails.isAccountNonExpired(), userDetails.isAccountNonLocked(), userDetails.isCredentialsNonExpired());
    }

    // UserDetails Methods

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return new ArrayList<>(this.authoritiesImpl);
    }

    // Getter, Setter

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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Set<AuthorityImpl> getAuthoritiesImpl() {
        return authoritiesImpl;
    }

    public void setAuthoritiesImpl(Set<AuthorityImpl> authoritiesImpl) {
        this.authoritiesImpl = authoritiesImpl;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    /**
     * Converts every {@link GrantedAuthority} to {@link AuthorityImpl} and sorts them
     *
     * @param authorities a {@link Collection} of Granted Authorities or its SubClasses
     * @return a SortedSet of the granted Authorities
     */
    private static SortedSet<AuthorityImpl> convertAndSortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        SortedSet<AuthorityImpl> sortedAuthorities = new TreeSet<>();

        for (GrantedAuthority grantedAuthority : authorities) {
            Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(AuthorityImpl.of(grantedAuthority));
        }
        return sortedAuthorities;

    }

    public void setFromUserDetails(UserDetails userDetails) {
        this.setPassword(userDetails.getPassword());
        this.setAuthoritiesImpl(convertAndSortAuthorities(userDetails.getAuthorities()));
        this.setUsername((userDetails.getUsername()));
        this.setEnabled(userDetails.isEnabled());
        userDetails.isCredentialsNonExpired();
        userDetails.isAccountNonExpired();
        userDetails.isAccountNonLocked();
    }
}
