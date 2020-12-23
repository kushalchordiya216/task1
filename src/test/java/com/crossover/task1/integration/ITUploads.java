package com.crossover.task1.integration;

import com.crossover.task1.controllers.UploadsController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ITUploads {
	@Autowired
	private UploadsController uploadsController;

}
