package com.crossover.task1.interfaces;

import com.crossover.task1.exceptions.ServerSideException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {
    List<String> store(MultipartFile file, String type) throws ServerSideException;
}
