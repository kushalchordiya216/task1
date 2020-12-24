package com.crossover.task1.exceptions;

/*
* Custom exception thrown whenever there is an internal service failure such as DB or storage service failure
* */
public class InternalException extends Exception{
    private final String message;
    public InternalException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
