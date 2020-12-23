package com.crossover.task1.unit;

import com.crossover.task1.exceptions.InternalException;
import com.crossover.task1.services.AWSStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import static org.mockito.BDDMockito.when;
import static org.mockito.BDDMockito.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@AutoConfigureMockMvc
class AWSStorageServiceTest {
    @Autowired
    AWSStorageService storageService;

    @MockBean
    S3Client s3Client;


    @Test
    void TestSuccessfulUpload() throws InternalException {
        MockMultipartFile file = new MockMultipartFile("file", "src/resources/assets/Images/earth.jpg",
                "image/png", "Spring Framework".getBytes());

        when(this.s3Client.putObject(any(PutObjectRequest.class),any(RequestBody.class)))
                .thenReturn(null);
        assertThat(storageService.store(file,"image/png")).isExactlyInstanceOf(String.class);
    }

    @Test
    void TestUnsuccessfulUpload() {
        MockMultipartFile file = new MockMultipartFile("file", "src/resources/assets/Images/earth.jpg",
                "image/jpeg", "Spring Framework".getBytes());
        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenThrow(new RuntimeException("error"));
        assertThatThrownBy(() -> this.storageService.store(file,"image/jpeg"))
                .isInstanceOf(InternalException.class)
                .hasMessage("Server Error during storing file! Upload unsuccessful");
    }


}
