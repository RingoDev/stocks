package com.ringodev.stocks.data;

public class EmailAlreadyExistsException extends AlreadyExistsException{

    public EmailAlreadyExistsException() {
        super();
    }

    public EmailAlreadyExistsException(String msg) {
        super(msg);
    }
}
