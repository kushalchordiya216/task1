package com.crossover.task1.unit;

import com.crossover.task1.entities.Metadata;
import com.crossover.task1.exceptions.InternalException;
import com.crossover.task1.exceptions.ValidationException;
import com.crossover.task1.interfaces.IStorageService;
import com.crossover.task1.interfaces.IMetadataService;
import com.crossover.task1.interfaces.IMediaValidatorService;
import com.crossover.task1.services.UploadsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@SpringBootTest
@AutoConfigureMockMvc
class UploadsServiceTest {
    @Autowired
    UploadsService uploadsService;

    @MockBean
    IStorageService storageService;
    @MockBean
    IMetadataService metadataService;
    @MockBean
    IMediaValidatorService validatorService;


    public UploadsServiceTest() {}


    @Test
    void passWhenAllServicesFunctionCorrectly() throws ValidationException, InternalException, IOException {
        MultipartFile multipartFile = new MockMultipartFile("image.jpeg","earth-large.jpg","image/jpeg",
                new FileInputStream("src/main/resources/assets/Images/earth-large.jpg"));

        when(validatorService.validate(multipartFile)).thenReturn(true);
        when(storageService.store(multipartFile,"image/jpeg")).thenReturn("objKey");
        when(metadataService.save("objKey","description","image/jpeg", multipartFile.getSize()))
                .thenReturn(new Metadata("objKey","description","image/jpeg", multipartFile.getSize()));

        assertThat(this.uploadsService.upload(multipartFile,"description")).isTrue();
        verify(validatorService, times(1))
                .validate(any(MultipartFile.class));
        verify(storageService, times(1))
                .store(any(MultipartFile.class), any(String.class));
        verify(metadataService, times(1))
                .save(any(String.class),any(String.class),any(String.class),any(Long.class));
    }

    @Test
    void rollbackStorageWhenDBFails() throws IOException, ValidationException, InternalException {
        MultipartFile multipartFile = new MockMultipartFile("image.jpeg","earth-large.jpg","image/jpeg",
                new FileInputStream("src/main/resources/assets/Images/earth-large.jpg"));

        when(validatorService.validate(multipartFile)).thenReturn(true);
        when(storageService.store(multipartFile,"image/jpeg")).thenReturn("objKey");
        when(metadataService.save("objKey","description","image/jpeg", multipartFile.getSize()))
                .thenThrow(new RuntimeException("DB Failure"));

        assertThatThrownBy(()->this.uploadsService.upload(multipartFile,"description"))
                .isInstanceOf(InternalException.class)
                .hasMessage("Error while saving metadata to database! Upload has been rolled back");

        verify(validatorService, times(1))
                .validate(any(MultipartFile.class));
        verify(storageService, times(1))
                .store(any(MultipartFile.class), any(String.class));
        verify(metadataService, times(1))
                .save(any(String.class),any(String.class),any(String.class),any(Long.class));
        verify(storageService, times(1))
                .delete(any(String.class));
    }



}
