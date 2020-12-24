package com.crossover.task1.services;

import com.crossover.task1.exceptions.ValidationException;
import com.crossover.task1.interfaces.IMediaValidatorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * implements te IMediaValidatorService for Images
 * Reads a image.maxsize variable from the environment
 * and stores it into maxAllowedFileSize variable
 */
@Service
public class ImageValidatorService implements IMediaValidatorService {
    @Value("${image.maxsize}")
    private long maxAllowedFileSize;

    /**
     * Takes in a multipart file and checks it's contentType and size
     * If the file is withing the correct size bounds and the correct contentType returns true,
     * else throws validation exception
     * @param file Multipart file, the file which is to be validated
     * @return true is the file is valid and satisfies all constraints
     * @throws ValidationException if the file is invalid , i.e., violates any of the constraints
     */
    @Override
    public boolean validate(MultipartFile file) throws ValidationException {
        String contentType = file.getContentType();
        long fileSize = file.getSize();

        if(file.getSize() == 0){
            throw new ValidationException("File is empty!");
        }
        if(fileSize > maxAllowedFileSize){
            throw new ValidationException("File size violates constraints!");
        }
        if(contentType == null || !(contentType.equals("image/jpeg") || contentType.equals("image/png"))){
            throw new ValidationException("File must be either JPG or PNG Image, other formats are not accepted!");
        }
        return true;
    }
}
