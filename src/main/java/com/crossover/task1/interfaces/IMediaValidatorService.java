package com.crossover.task1.interfaces;

import com.crossover.task1.exceptions.ValidationException;
import org.springframework.web.multipart.MultipartFile;

public interface IMediaValidatorService {
    boolean validate(MultipartFile file) throws ValidationException;
}
