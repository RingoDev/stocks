package com.ringodev.stocks.data;

public class AlreadyExistsException extends Exception{

    public AlreadyExistsException() {
        super();
    }
    public AlreadyExistsException(String msg){
        super(msg);
    }
}
