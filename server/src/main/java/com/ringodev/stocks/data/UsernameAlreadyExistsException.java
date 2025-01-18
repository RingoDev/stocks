package com.ringodev.stocks.data;

public class UsernameAlreadyExistsException extends AlreadyExistsException {

    public UsernameAlreadyExistsException(String msg){
        super(msg);
    }
}
