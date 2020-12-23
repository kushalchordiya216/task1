package com.crossover.task1.exceptions;

public class ValidationException extends Exception {

    private final String message;

    public ValidationException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() { return this.message; }

}
