package com.crossover.task1.interfaces;

import com.crossover.task1.exceptions.ValidationException;
import org.springframework.web.multipart.MultipartFile;

/*
* Interface to be used for validating any kind of multimedia uploads
* Specific services should implement this interface for specific file types
* with specific constraints on what constitutes a valid file
* */
public interface IMediaValidatorService {
    boolean validate(MultipartFile file) throws ValidationException;
}
