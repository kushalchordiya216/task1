package com.crossover.task1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
class AWSStorageConfiguration {

    @Bean
    public S3Client getAmazonS3Client(){
        return S3Client.builder()
                .region(Region.AP_SOUTH_1)
                .build();
    }

}
