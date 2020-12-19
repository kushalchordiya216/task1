package com.crossover.task1.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    public void store(MultipartFile file);
}
