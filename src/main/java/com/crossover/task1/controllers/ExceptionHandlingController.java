package com.crossover.task1.controllers;

import com.crossover.task1.exceptions.InternalException;
import com.crossover.task1.exceptions.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletException;

@ControllerAdvice
/*
* Global Exception handling controller
* Handles any and all exceptions thrown across the application,
* Assigns the proper statusCode to outgoing response
* along with the exception message as response body
* */
public class ExceptionHandlingController {

    @ExceptionHandler({ValidationException.class, ServletException.class})
    public ResponseEntity<String> handleValidationError(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalException.class)
    public ResponseEntity<String> handleInternalError(InternalException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnknownError(Exception ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
