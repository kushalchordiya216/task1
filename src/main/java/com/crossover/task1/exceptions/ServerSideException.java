package com.crossover.task1.exceptions;

public class ServerSideException extends Exception{
    private final String message;
    public ServerSideException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
