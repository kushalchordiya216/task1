package com.crossover.task1.controllers;

import com.crossover.task1.interfaces.MediaHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadsController {
    private final MediaHandler mediaHandler;

    @Autowired
    public UploadsController(MediaHandler mediaHandler){
        this.mediaHandler = mediaHandler;
    }

    @PostMapping("/images")
    public String handleFileUpload(@RequestParam("file")MultipartFile file, String description){
        if(!this.mediaHandler.validate(file)){
           return "File violates constraints, upload unsuccessful!";
        }
        return "File is valid, upload successful!";
    }
}
