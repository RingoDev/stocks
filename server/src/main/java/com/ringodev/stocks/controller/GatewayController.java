package com.ringodev.stocks.controller;

import com.ringodev.stocks.service.user.UserDetailsManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;


@RestController
@RequestMapping("api")
public class GatewayController {

    UserDetailsManagerImpl userService;

    @Autowired
    GatewayController(UserDetailsManagerImpl userService) {
        this.userService = userService;
    }


    // tries to signup a new user
    @PostMapping("/signup")
    public ResponseEntity<Object> signup(HttpServletRequest request) {
        User user = new User(request.getParameter("username"), request.getParameter("password"), new ArrayList<>());
        userService.createUser(user);
        System.out.println("ADDED USER" + user.toString());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // gets the userdata of the user
    @GetMapping("/user/data")
    public ResponseEntity<Object> getData(Principal principal) {
        Authentication uAuth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(uAuth);
        System.out.println(principal.getName());
        System.out.println(principal.toString());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
