package com.ringodev.stocks.controller;

import com.ringodev.stocks.data.AlreadyExistsException;
import com.ringodev.stocks.data.Position;
import com.ringodev.stocks.service.stocks.StocksService;
import com.ringodev.stocks.service.user.*;
import com.ringodev.stocks.service.userdata.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * Used to insert some custom users for testing
 */
@Component
public class TestCommandLineRunner implements CommandLineRunner {

    private final UserDetailsManagerImpl userService;
    private final UserDataService userDataService;
    private final PasswordEncoder passwordEncoder;
    private final StocksService stocksService;

    @Autowired
    TestCommandLineRunner(StocksService stocksService,UserDataService userDataService,UserDetailsManagerImpl userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userDataService = userDataService;
        this.stocksService = stocksService;
    }

    @Override
    public void run(String... args) throws AlreadyExistsException {

        // add TestUsers
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

        // insert Stockdata
        stocksService.insertStock("GM");
        stocksService.insertStock("JNJ");

        // add TestPosition

        userDataService.addPosition(new Position("GM",new Date(2015-1900, Calendar.APRIL,28),50),user2.getUsername());
        userDataService.addPosition(new Position("JNJ",new Date(2016-1900, Calendar.APRIL,28),10),user2.getUsername());
    }
}
