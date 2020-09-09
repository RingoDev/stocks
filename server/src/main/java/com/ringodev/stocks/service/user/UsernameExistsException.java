package com.ringodev.stocks.service.user;

public class UsernameExistsException extends Exception{

    UsernameExistsException(String msg){
        super(msg);
    }
}
