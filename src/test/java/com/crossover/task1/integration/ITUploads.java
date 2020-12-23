package com.crossover.task1.integration;

import com.crossover.task1.Task1Application;
import com.crossover.task1.config.TestDBConfiguration;
import com.crossover.task1.entities.Metadata;
import com.crossover.task1.repository.MetadataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.FileInputStream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.any;


@SpringBootTest(classes = {Task1Application.class, TestDBConfiguration.class})
@AutoConfigureMockMvc
class ITUploads {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	S3Client s3Client;
	@MockBean
	MetadataRepository repository;

	@Test
	void successFullyCreateResourceReturn201() throws Exception {
		when(this.s3Client.putObject(any(PutObjectRequest.class),any(RequestBody.class)))
				.thenReturn(null);
		MockMultipartFile multipartFile = new MockMultipartFile("file","earth.jpg","image/jpeg",
				new FileInputStream("src/main/resources/assets/Images/earth.jpg"));

		this.mockMvc.perform(multipart("/api/images").file(multipartFile)
				.param("description","my image"))
				.andExpect(status().isCreated())
				.andExpect(content().string(containsString("File successfully uploaded!")));

		//verify both storage and metadataDB calls are made
		verify(s3Client,times(1)).putObject(any(PutObjectRequest.class),any(RequestBody.class));
		verify(repository, times(1)).save(any(Metadata.class));
	}

	@Test
	void failureDueToFileConstraintViolationReturns400() throws Exception {

		MockMultipartFile largeFile = new MockMultipartFile("file","earth-large.jpg","image/jpeg",
				new FileInputStream("src/main/resources/assets/Images/earth-large.jpg"));
		MockMultipartFile wrongFile = new MockMultipartFile("file","earth.ico","image/x-icon",
				new FileInputStream("src/main/resources/assets/Images/earth.ico"));


		//size constraint violation
		this.mockMvc.perform(multipart("/api/images").file(largeFile)
				.param("description","large image"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("File size violates constraints!")));

		//file format constrain violation
		this.mockMvc.perform(multipart("/api/images").file(wrongFile)
				.param("description","wrong image"))
				.andExpect((status().isBadRequest()))
				.andExpect(content().string(containsString
						("File must be either JPG or PNG Image, other formats are not accepted!"))
				);
	}

	@Test
	void failureDueToIncorrectRequestParameters() throws Exception{
		MockMultipartFile correctFile = new MockMultipartFile("file","earth.jpg","image/jpeg",
				new FileInputStream("src/main/resources/assets/Images/earth.jpg"));

		//missing description
		this.mockMvc.perform(multipart("/api/images").file(correctFile))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(
						containsString("Required String parameter 'description' is not present"))
				);

		//missing file upload
		this.mockMvc.perform(multipart("/api/images").param("description","no image"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Required request part 'file' is not present")));

		//blank description
		this.mockMvc.perform(multipart("/api/images").file(correctFile).param("description",""))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Description field is empty")));

	}

	@Test
	void failsDueToStorageFailureReturns500() throws Exception{
		given(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
				.willThrow(new RuntimeException("Error during S3 storage"));

		MockMultipartFile multipartFile = new MockMultipartFile("file","earth.jpg","image/jpeg",
				new FileInputStream("src/main/resources/assets/Images/earth.jpg"));

		this.mockMvc.perform(multipart("/api/images").file(multipartFile)
				.param("description","image"))
				.andExpect(status().isInternalServerError())
				.andExpect(content().string(containsString("Server Error during storing file! Upload unsuccessful")));

	}

	@Test
	void failsDueToDatabaseFailureReturns500() throws Exception{

		MockMultipartFile multipartFile = new MockMultipartFile("file","earth.jpg","image/jpeg",
				new FileInputStream("src/main/resources/assets/Images/earth.jpg"));

		when(repository.save(any(Metadata.class))).thenThrow(new RuntimeException("database op failed"));
		this.mockMvc.perform(multipart("/api/images").file(multipartFile)
				.param("description","image"))
				.andExpect(status().isInternalServerError())
				.andExpect(content().string(
						containsString("Error while saving metadata to database! Upload has been rolled back"))
				);

		//verify S3 record is rolled back after DB failure
		verify(s3Client,times(1)).deleteObject(any(DeleteObjectRequest.class));
	}
}
