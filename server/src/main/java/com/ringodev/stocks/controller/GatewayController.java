package com.ringodev.stocks.controller;

import com.ringodev.stocks.service.user.MyUserDetailsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;


@RestController
@RequestMapping("api")
public class GatewayController {

    MyUserDetailsManager userService;

    @Autowired
    GatewayController(MyUserDetailsManager userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Object> signup(HttpServletRequest request) {
        User user = new User(request.getParameter("username"),request.getParameter("password"),new ArrayList<>());
        userService.createUser(user);
        System.out.println("ADDED USER" + user.toString());
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
