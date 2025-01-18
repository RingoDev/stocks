package com.ringodev.stocks.controller;

import com.ringodev.stocks.data.AlreadyExistsException;
import com.ringodev.stocks.data.Position;
import com.ringodev.stocks.service.mail.MailService;
import com.ringodev.stocks.service.stocks.StocksService;
import com.ringodev.stocks.service.user.AuthorityImpl;
import com.ringodev.stocks.service.user.UserDetailsManagerImpl;
import com.ringodev.stocks.service.user.UserImpl;
import com.ringodev.stocks.service.userdata.UserDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.task.TaskExecutor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Used to insert some custom users for testing
 */
@Component
public class TestCommandLineRunner implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(TestCommandLineRunner.class);

    private final MailService mailService;
    private final UserDetailsManagerImpl userService;
    private final UserDataService userDataService;
    private final PasswordEncoder passwordEncoder;
    private final StocksService stocksService;
    private final TaskExecutor taskExecutor;

    @Autowired
    TestCommandLineRunner(MailService mailService, StocksService stocksService, UserDataService userDataService, UserDetailsManagerImpl userService, PasswordEncoder passwordEncoder, TaskExecutor taskExecutor) {
        this.userService = userService;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
        this.userDataService = userDataService;
        this.stocksService = stocksService;
        this.taskExecutor = taskExecutor;
    }

    @Override
    public void run(String... args) {

        logger.info("Running TestCommandLineRunner");

        deleteUsers();

        addTestUsers();

        // insert Stockdata in new Thread
        taskExecutor.execute(() -> stocksService.insertStocks("/home/data/stock_data_test/"));

//        mailService.sendSimpleMessage("ruil4official@gmail.com", "Test", "Test successful");
    }

    private void addTestUsers() {
        List<List<String>> testUserList = List.of(List.of("admin","password","ADMIN","admin.ringodev@protonmail.com"),
                List.of("user","password","USER","user.ringodev@protonmail.com")
        );

        for (List<String> list : testUserList) {

            UserDetails user = new UserImpl(list.get(0),passwordEncoder.encode(list.get(1)),List.of(new AuthorityImpl(list.get(2))));

            addUser(user);

            addTestPositions(list, user);

            verifyUser(list, user);

            logger.info("added User: " + userDataService.getUserData(user.getUsername()).toString());
        }

        logger.info("TestUserData: "+userDataService.getAllUserData());
    }

    private void verifyUser(List<String> list, UserDetails user) {
        userService.verifyUser(user.getUsername());
        userDataService.setVerifiedEmail(user.getUsername(), list.get(3));
    }

    private void deleteUsers() {
        logger.info("Deleting all Users and Userdata");
        userDataService.clearALL();
        userService.clearALL();
    }

    private void addTestPositions(List<String> list, UserDetails user) {
        if (userDataService.getUserData(user.getUsername()) == null) {
            try {
                logger.info("Adding UserData for User: "+ user.getUsername());
                userDataService.createUserData(user);
            } catch (AlreadyExistsException e) {
                logger.warn("UserData already existed for user: " + user.getUsername());
            }
        }
        userDataService.addPosition(new Position("GM", new Date(2015 - 1900, Calendar.APRIL, 28), 50), user.getUsername());
        userDataService.addPosition(new Position("JNJ", new Date(2016 - 1900, Calendar.APRIL, 28), 10), user.getUsername());
    }

    private void addUser(UserDetails user) {
        logger.info("Adding User: "+ user.getUsername());
        if (userService.userExists(user.getUsername())) {
            logger.warn(user.getUsername() + " already exists and cant be inserted");
        } else {
            userService.createUser(user);
        }
    }

}
