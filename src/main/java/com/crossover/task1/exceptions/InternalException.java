package com.crossover.task1.exceptions;

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
