package com.ringodev.stocks.controller;

import com.ringodev.stocks.data.AlreadyExistsException;
import com.ringodev.stocks.data.Position;
import com.ringodev.stocks.service.stocks.StocksService;
import com.ringodev.stocks.service.user.AuthorityImpl;
import com.ringodev.stocks.service.user.Role;
import com.ringodev.stocks.service.user.UserDetailsManagerImpl;
import com.ringodev.stocks.service.user.UserImpl;
import com.ringodev.stocks.service.userdata.UserDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.task.TaskExecutor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * Used to insert some custom users for testing
 */
@Component
public class TestCommandLineRunner implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(TestCommandLineRunner.class);

    private final UserDetailsManagerImpl userService;
    private final UserDataService userDataService;
    private final PasswordEncoder passwordEncoder;
    private final StocksService stocksService;
    private final TaskExecutor taskExecutor;

    @Autowired
    TestCommandLineRunner(StocksService stocksService,UserDataService userDataService,UserDetailsManagerImpl userService, PasswordEncoder passwordEncoder, TaskExecutor taskExecutor) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userDataService = userDataService;
        this.stocksService = stocksService;
        this.taskExecutor = taskExecutor;
    }

    @Override
    public void run(String... args) throws AlreadyExistsException {

//         add TestUsers
        UserImpl user2 = new UserImpl("admin@gmail.com", passwordEncoder.encode("password"),new AuthorityImpl(Role.ROLE_ADMIN));
        UserImpl user3 = new UserImpl("user@gmail.com", passwordEncoder.encode("password"),new AuthorityImpl(Role.ROLE_USER));

        if(userService.userExists(user2.toUserDetails().getUsername())){
            logger.warn(user2.toUserDetails().getUsername()+ " already exists and cant be inserted");
        }else{
            userService.createUser(user2.toUserDetails());
        }

        if(userService.userExists(user3.toUserDetails().getUsername())){
            logger.warn(user2.toUserDetails().getUsername()+ " already exists and cant be inserted");
        }else{
            userService.createUser(user3.toUserDetails());
        }


        // only add testpositions if they dont exist
        if(userDataService.getUserData(user2.getUsername()) == null){
            try{
                userDataService.createUserData(user2.toUserDetails());
            }catch(AlreadyExistsException e){
                logger.warn("UserData already existed for user: "+ user2.toUserDetails().getUsername());
            }
            try{
                userDataService.createUserData(user3.toUserDetails());
            }catch(AlreadyExistsException e){
                logger.warn("UserData already existed for user: "+ user3.toUserDetails().getUsername());
            }
        }


        for (UserImpl user : userService.getAll()) {
            logger.info("added User: " + userDataService.getUserData(user.toUserDetails().getUsername()).toString());
        }

        // insert Stockdata in new Thread
        taskExecutor.execute(() -> stocksService.insertStocks("data/stock_data_test/"));

        // add TestPosition

        userDataService.addPosition(new Position("GM",new Date(2015-1900, Calendar.APRIL,28),50),user2.getUsername());
        userDataService.addPosition(new Position("JNJ",new Date(2016-1900, Calendar.APRIL,28),10),user2.getUsername());
    }
}
