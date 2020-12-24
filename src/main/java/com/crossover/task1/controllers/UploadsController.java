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
/*
* The main controller that exposes a POST endpoint to which images can be uploaded
* Controller itself only makes sure the incoming request is correct formed
* with proper request parameters
* Controller has an UploadsService service dependency that handles all business logic
* Returns an HTTP 201 status code when request succeeds
* */
public class UploadsController {
    @Autowired
    private UploadsService uploadsService;


    /**
     * Takes in a POST request, validated the correct parameters have been passed and
     * delegated the work to business services
     * @param file Multipart file sent as a request parameter in the POST request
     * @param description description of file, sent as a request parameter
     * @return Status code 201 and string message indicating successful upload operation
     * @throws ValidationException if POST request or file is invalid
     * @throws InternalException if internal services fail
     */
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
