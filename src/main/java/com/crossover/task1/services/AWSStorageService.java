package com.crossover.task1.services;

import com.crossover.task1.exceptions.ServerSideException;
import com.crossover.task1.interfaces.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@Service
public class AWSStorageService implements StorageService {
    @Autowired
    private S3Client s3Client;
    @Value("${aws.bucketName}")
    private String bucketName;

    @Override
    public ArrayList<String> store(MultipartFile file, String contentType) throws ServerSideException {
        String suffix = "";
        if(contentType == null || contentType.equals("image/jpeg")){
            suffix = ".jpg";
        }else if(contentType.equals("image/png")){
            suffix=".png";
        }
        String objKey = UUID.randomUUID().toString() + suffix;

        try{
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName).key(objKey).build();
            PutObjectResponse response = s3Client
                    .putObject(objectRequest, RequestBody.fromByteBuffer(ByteBuffer.wrap(file.getBytes())));
            return new ArrayList<>(Arrays.asList(objKey, bucketName, response.eTag()));
        } catch (SdkException | IOException ex){
            throw new ServerSideException("Server Error during storing file! Upload unsuccessful");
        }
    }

}
