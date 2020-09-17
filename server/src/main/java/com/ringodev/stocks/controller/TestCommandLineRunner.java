package com.ringodev.stocks.controller;

import com.ringodev.stocks.data.AlreadyExistsException;
import com.ringodev.stocks.service.user.*;
import com.ringodev.stocks.service.userdata.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Used to insert some custom users for testing
 */
@Component
public class TestCommandLineRunner implements CommandLineRunner {

    private final UserDetailsManagerImpl userService;
    private final UserDataService userDataService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    TestCommandLineRunner(UserDataService userDataService,UserDetailsManagerImpl userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userDataService = userDataService;
    }

    @Override
    public void run(String... args) throws AlreadyExistsException {

        UserImpl user1 = new UserImpl("admin", passwordEncoder.encode("password"), new AuthorityImpl(Role.ROLE_ADMIN));
        UserImpl user2 = new UserImpl("admin@gmail.com", passwordEncoder.encode("password"),new AuthorityImpl(Role.ROLE_ADMIN));
        UserImpl user3 = new UserImpl("user", passwordEncoder.encode("password"),new AuthorityImpl(Role.ROLE_USER));

        userService.createUser(user1.toUserDetails());
        userService.createUser(user2.toUserDetails());
        userService.createUser(user3.toUserDetails());

        userDataService.createUserData(user1.toUserDetails());
        userDataService.createUserData(user2.toUserDetails());
        userDataService.createUserData(user3.toUserDetails());

        for (UserImpl user : userService.getAll()) {
            System.out.println(user.toUserDetails());
            System.out.println(userDataService.getUserData(user.toUserDetails().getUsername()));
        }
    }
}
