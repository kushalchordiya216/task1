package com.crossover.task1.interfaces;

import com.crossover.task1.exceptions.InternalException;
import org.springframework.web.multipart.MultipartFile;

/*
* Interface for storing and deleting objects being uploaded
* Interface should be implemented by specific services to
* interact with specific storage platforms
* */
public interface IStorageService {
    String store(MultipartFile file, String type) throws InternalException;
    void delete(String objKey);

}
