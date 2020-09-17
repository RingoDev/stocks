package com.ringodev.stocks.controller;

import com.ringodev.stocks.data.AlreadyExistsException;
import com.ringodev.stocks.service.user.UserDetailsManagerImpl;
import com.ringodev.stocks.service.userdata.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;


@RestController
@RequestMapping("api")
public class GatewayController {

    private final UserDetailsManagerImpl userService;
    private final UserDataService userDataService;


    @Autowired
    GatewayController(UserDetailsManagerImpl userService,UserDataService userDataService) {
        this.userDataService = userDataService;
        this.userService = userService;
    }


    // tries to signup a new user
    @PostMapping("/signup")
    public ResponseEntity<Object> signup(HttpServletRequest request) {
        User user = new User(request.getParameter("username"), request.getParameter("password"), new ArrayList<>());
        try{
            userService.createUser(user);
            userDataService.createUserData(user);
        }catch(AlreadyExistsException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        System.out.println("ADDED USER" + user.toString());
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
