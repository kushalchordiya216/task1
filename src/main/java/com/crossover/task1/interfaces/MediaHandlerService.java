package com.crossover.task1.interfaces;

import com.crossover.task1.exceptions.ClientSideException;
import org.springframework.web.multipart.MultipartFile;

public interface MediaHandlerService {
    boolean validate(MultipartFile file) throws ClientSideException;
}
