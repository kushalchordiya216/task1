package com.crossover.task1.exceptions;

/*
* Custom exception thrown whenever there is a client error, such as incorrect request format
* or incorrect files being uploaded.
* */
public class ValidationException extends Exception {

    private final String message;

    public ValidationException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() { return this.message; }

}
