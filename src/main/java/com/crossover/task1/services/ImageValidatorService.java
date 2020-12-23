package com.crossover.task1.services;

import com.crossover.task1.exceptions.ValidationException;
import com.crossover.task1.interfaces.IMediaValidatorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageValidatorService implements IMediaValidatorService {
    @Value("${image.maxsize}")
    private long maxAllowedFileSize;

    @Override
    public boolean validate(MultipartFile file) throws ValidationException {
        if(file.getSize() == 0){
            throw new ValidationException("File is empty!");
        }
        String contentType = file.getContentType();

        long fileSize = file.getSize();
        if(fileSize == 0 || fileSize > maxAllowedFileSize){
            throw new ValidationException("File size violates constraints!");
        }
        if(contentType == null || !(contentType.equals("image/jpeg") || contentType.equals("image/png"))){
            throw new ValidationException("File must be either JPG or PNG Image, other formats are not accepted!");
        }
        return true;
    }
}
