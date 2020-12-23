package com.crossover.task1.interfaces;

import com.crossover.task1.exceptions.InternalException;
import org.springframework.web.multipart.MultipartFile;

public interface IStorageService {
    String store(MultipartFile file, String type) throws InternalException;
    void delete(String objKey);

}
