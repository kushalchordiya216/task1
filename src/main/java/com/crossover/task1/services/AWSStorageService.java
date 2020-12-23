package com.crossover.task1.services;

import com.crossover.task1.exceptions.InternalException;
import com.crossover.task1.interfaces.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

@Service
public class AWSStorageService implements IStorageService {
    @Autowired
    private S3Client s3Client;
    @Value("${aws.bucketName}")
    private String bucketName;

    @Override
    public String store(MultipartFile file, String contentType) throws InternalException {
        String suffix = "";
        if(contentType.equals("image/jpeg")){
            suffix = ".jpg";
        }
        if(contentType.equals("image/png")){
            suffix = ".png";
        }
        String objKey = UUID.randomUUID().toString() + suffix;

        try{
            PutObjectRequest objectRequest = PutObjectRequest.builder().bucket(bucketName).key(objKey).build();
            s3Client.putObject(objectRequest, RequestBody.fromByteBuffer(ByteBuffer.wrap(file.getBytes())));
            return objKey;
        } catch (RuntimeException | IOException ex){
            throw new InternalException("Server Error during storing file! Upload unsuccessful");
        }
    }

    @Override
    public void delete(String objKey) { this.s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(objKey).build()); }

}
