package com.crossover.task1;

import com.crossover.task1.controllers.UploadsController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ITUploads {
	@Autowired
	private UploadsController uploadsController;

	@Test
	void contextLoads() {
		assertThat(uploadsController).isNotNull();
	}

}
