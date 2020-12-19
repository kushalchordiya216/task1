package com.crossover.task1.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface MediaHandler {
    public boolean validate(MultipartFile file);
}
