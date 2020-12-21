package com.crossover.task1.services;

import com.crossover.task1.exceptions.ClientSideException;
import com.crossover.task1.interfaces.MediaHandlerService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageHandlerService implements MediaHandlerService {

    @Override
    public boolean validate(MultipartFile file) throws ClientSideException {
        String contentType = file.getContentType();
        //application properties already sets maximum multipart size to 500KB, this is just an additional check

        long maxAllowedFileSize = 512000; //500 KB converted to bytes
        long fileSize = file.getSize();
        if(fileSize == 0 || fileSize > maxAllowedFileSize){
            throw new ClientSideException("File size violates constraints!");
        }
        if(contentType == null || !(contentType.equals("image/jpeg") || contentType.equals("image/png"))){
            throw new ClientSideException("File must be either JPG or PNG Image, other formats are not accepted!");
        }
        return true;
    }
}
