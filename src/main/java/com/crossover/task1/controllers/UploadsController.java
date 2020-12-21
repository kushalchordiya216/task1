package com.crossover.task1.controllers;

import com.crossover.task1.exceptions.ClientSideException;
import com.crossover.task1.exceptions.ServerSideException;
import com.crossover.task1.interfaces.MediaHandlerService;
import com.crossover.task1.interfaces.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MissingServletRequestPartException;


@RestController
public class UploadsController {
    private final MediaHandlerService mediaHandlerService;
    private final StorageService storageService;

    @Autowired
    public UploadsController(MediaHandlerService mediaHandlerService, StorageService storageService){
        this.mediaHandlerService = mediaHandlerService;
        this.storageService = storageService;
    }

    @PostMapping("/api/images")
    public ResponseEntity<String> handleFileUpload
            (@RequestParam("file")MultipartFile file, @RequestParam("description") String description) throws ClientSideException, ServerSideException {
        if(description.equals("")){
            throw new ClientSideException("Description field is missing!");
        }
        this.mediaHandlerService.validate(file);
        this.storageService.store(file, file.getContentType());
        return new ResponseEntity<>("File successfully uploaded!", HttpStatus.CREATED);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleParameterNotFound(MissingServletRequestParameterException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<String> handleFileNotFound(MissingServletRequestPartException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ClientSideException.class)
    public ResponseEntity<String> handleClientSideError(ClientSideException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServerSideException.class)
    public ResponseEntity<String> handleServerSideError(ServerSideException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
