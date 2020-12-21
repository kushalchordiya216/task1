package com.crossover.task1.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
class AWSStorageConfiguration {
    // bucketName will be read from the application.properties file  during the application initialization.

    @Value("${aws.bucketName}")
    private String bucketName;

    @Bean
    public S3Client getAmazonS3Client(){
        return S3Client.builder()
                .region(Region.AP_SOUTH_1)
                .build();
    }

}
