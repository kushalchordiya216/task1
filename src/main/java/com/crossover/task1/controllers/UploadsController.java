package com.crossover.task1.controllers;

import com.crossover.task1.exceptions.ValidationException;
import com.crossover.task1.exceptions.InternalException;
import com.crossover.task1.services.UploadsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadsController {
    @Autowired
    private UploadsService uploadsService;


    @PostMapping("/api/images")
    public ResponseEntity<String> handleFileUpload (
            @RequestParam("file") MultipartFile file,
            @RequestParam("description") String description
    ) throws ValidationException, InternalException
    {
        if(description.equals("")){
            throw new ValidationException("Description field is empty!");
        }
        this.uploadsService.upload(file, description);
        return new ResponseEntity<>("File successfully uploaded!", HttpStatus.CREATED);
    }

}
