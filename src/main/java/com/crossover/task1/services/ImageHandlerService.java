package com.crossover.task1.services;

import com.crossover.task1.interfaces.MediaHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageHandlerService implements MediaHandler {

    @Override
    public boolean validate(MultipartFile file) {
        return false;
    }
}
