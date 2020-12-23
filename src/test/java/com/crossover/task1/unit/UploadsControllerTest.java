package com.crossover.task1.unit;

import com.crossover.task1.services.UploadsService;
import org.junit.jupiter.api.Test;
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


@SpringBootTest
@AutoConfigureMockMvc
class UploadsControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UploadsService uploadsService;

    @Test
    void shouldFailWhenFileMissing() throws Exception {
        this.mvc.perform(multipart("/api/images").param("description","empty"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Required request part 'file' is not present")));
    }

    @Test
    void shouldFailWhenDescriptionMissing() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "earth.jpg",
                "image/jpg", "String".getBytes());
        this.mvc.perform(multipart("/api/images").file(multipartFile))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Required String parameter 'description' is not present")));
    }

    @Test
    void shouldFailWhenDescriptionEmpty() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "earth.jpg",
                "image/jpeg", "String".getBytes());
        this.mvc.perform(multipart("/api/images").file(multipartFile).param("description",""))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Description field is empty")));

    }

    @Test
    void shouldPassWhenBothFieldsPresent() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "resources/assets/Images/earth.jpg",
                "image/jpeg", "String".getBytes());

        given(this.uploadsService.upload(multipartFile, "description")).willReturn(true);
        this.mvc.perform(multipart("/api/images").file(multipartFile)
                .param("description","some image"))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("File successfully uploaded!")));
    }


}
