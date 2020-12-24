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

/**
* Implements the IStorageService interface for storing objects on AWS S3 buckets
* Has an S3 client as a dependency bean, injected into it, and the name of the bucket read from the properties file
* */
@Service
public class AWSStorageService implements IStorageService {
    @Autowired
    private S3Client s3Client;
    @Value("${aws.bucketName}")
    private String bucketName;

    /**
     * Takes in a multipart file and it's content type and generates a unique objKey for the file
     * Uploads the file to S3 using the S3Client injected bean and returns the generated objKey if operation succeds
     * Throws internal exception otherwise
     * @param file Multipart file, that is to be uploaded onto the S3 bucket
     * @param contentType content type of the file, example: image/jpeg
     * @return String objKey generated for the object
     * @throws InternalException to indicate failure during object storage
     */
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

    /**
     * Deletes a stored object from S3 bucket using the generated objKey
     * @param objKey objKey of the object to be deleted
     */
    @Override
    public void delete(String objKey) { this.s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(objKey).build()); }

}
