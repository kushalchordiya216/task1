package com.crossover.task1;

import com.crossover.task1.interfaces.MediaHandlerService;
import com.crossover.task1.interfaces.StorageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;


@SpringBootTest
@AutoConfigureMockMvc
class UploadsControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private MediaHandlerService mediaHandlerService;
    @MockBean
    private StorageService storageService;

    @Test
    void shouldFailWhenFileMissing() throws Exception {
        this.mvc.perform(multipart("/images").param("description","empty"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Required request part 'file' is not present")));
    }

    @Test
    void shouldFailWhenDescriptionMissing() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "earth.jpg",
                "image/jpg", "Spring Framework".getBytes());
        this.mvc.perform(multipart("/images").file(multipartFile))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Required String parameter 'description' is not present")));
    }

    @Test
    void shouldFailWhenDescriptionIsEmpty() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "resources/assets/Images/earth.jpg",
                "image/jpg", "Spring Framework".getBytes());

        given(this.mediaHandlerService.validate(multipartFile)).willReturn(true);
        this.mvc.perform(multipart("/images").file(multipartFile)
                .param("description",""))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Description field is missing")));
    }

    @Test
    void shouldPassWhenBothFieldsPresent() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "resources/assets/Images/earth.jpg",
                "image/jpg", "Spring Framework".getBytes());

        given(this.mediaHandlerService.validate(multipartFile)).willReturn(true);
        this.mvc.perform(multipart("/images").file(multipartFile)
                .param("description","some image"))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("File successfully uploaded!")));
        then(this.storageService).should().store(multipartFile, multipartFile.getName());
    }


}
