package com.crossover.task1.exceptions;

public class ClientSideException extends Exception {

    private final String message;

    public ClientSideException(String message){
        this.message = message;
    }


    @Override
    public String getMessage() { return this.message; }

}
