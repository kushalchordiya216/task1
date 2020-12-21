package com.crossover.task1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.crossover.task1.exceptions.ClientSideException;
import com.crossover.task1.services.ImageHandlerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.crossover.task1.exceptions.ClientSideException;

@SpringBootTest
class ImageHandlerServiceTest {
    @Autowired
    ImageHandlerService imageHandlerService;

    @Test
    void shouldFailWhenImageIsTooLarge() throws IOException {

        MultipartFile multipartFile = new MockMultipartFile("image.jpeg","earth-large.jpg","image/jpeg",
                new FileInputStream("src/main/resources/assets/Images/earth-large.jpg"));
        assertThatThrownBy(()-> {
            imageHandlerService.validate((multipartFile));
        }).isInstanceOf(ClientSideException.class).hasMessage("File size violates constraints!");
    }

    @Test
    void shouldFailWhenIncompatibleFileFormatUsed() throws IOException {

        MultipartFile multipartFile = new MockMultipartFile("image.ico","earth.ico","image/x-icon",
                new FileInputStream("src/main/resources/assets/Images/earth.ico"));
        assertThatThrownBy(()-> {
            imageHandlerService.validate((multipartFile));
        }).isInstanceOf(ClientSideException.class)
                .hasMessage("File must be either JPG or PNG Image, other formats are not accepted!");
    }

    @Test
    void shouldPassWhenConstraintsAreSatisfied() throws IOException, ClientSideException {
        MultipartFile multipartFile = new MockMultipartFile("image.png","earth.png","image/png",
                new FileInputStream("src/main/resources/assets/Images/earth.png"));
        assertThat(imageHandlerService.validate(multipartFile)).isTrue();
    }
}
