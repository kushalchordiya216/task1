package com.crossover.task1.unit;

import com.crossover.task1.Task1Application;
import com.crossover.task1.config.TestDBConfiguration;
import com.crossover.task1.entities.Metadata;
import com.crossover.task1.services.RDSMetadataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {Task1Application.class, TestDBConfiguration.class})
@ActiveProfiles("test")
class RDSMetaDataServiceTest {

    @Autowired
    RDSMetadataService metadataService;

    @Test
    void shouldFailWhenObjKeyIsNull(){
        assertThatThrownBy( () -> metadataService.save(null, "desc1","image/jpeg", 1000L)
        ).isInstanceOf(DataIntegrityViolationException.class).hasMessageContaining("objKey");
    }

    @Test
    void shouldFailWhenDescriptionIsNull(){
        assertThatThrownBy( () -> metadataService.save("key1", null,"image/jpeg", 1000L)
        ).isInstanceOf(DataIntegrityViolationException.class).hasMessageContaining("description");
    }

    @Test
    void shouldFailWhenObjKeyIsNotUnique(){
        metadataService.save("key_abc","desc_abc","image/png", 1000L);
        assertThatThrownBy( () -> metadataService.save("key_abc", "desc","image/jpeg", 1000L)
        ).isInstanceOf(DataIntegrityViolationException.class).hasMessageContaining("ConstraintViolation");
    }

    @Test
    void shouldPassWhenAllDataIsCorrect(){
        assertThat(metadataService.save("objKey","description","image/jpg",1000L))
                .isExactlyInstanceOf(Metadata.class);
        assertThat(metadataService.fetchAll().size()).isNotZero();
    }

}
