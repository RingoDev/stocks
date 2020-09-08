package com.ringodev.stocks.controller;

import com.ringodev.stocks.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@RestController
@RequestMapping("api")
public class GatewayController {
    static class SimpleUser {
        String name;
        String pass;

        @Override
        public String toString() {
            return "SimpleUser{" +
                    "name='" + name + '\'' +
                    '}';
        }

        SimpleUser() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPass() {
            return pass;
        }

        public void setPass(String pass) {
            this.pass = pass;
        }
    }


    UserService userService;

    @Autowired
    GatewayController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/addUser")
    @CrossOrigin(origins = "http://localhost:4200")
    public void addUser(@RequestBody SimpleUser newUser) {
        boolean result = userService.addUser(new User(newUser.getName(),newUser.getPass(),new ArrayList<>()));
        if (result)System.out.println("ADDED USER" + newUser.toString());
    }

}
