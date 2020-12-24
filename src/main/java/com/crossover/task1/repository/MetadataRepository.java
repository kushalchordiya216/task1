package com.crossover.task1.repository;

import com.crossover.task1.entities.Metadata;
import org.springframework.data.repository.CrudRepository;

/*
* Repository for handling DB CRUD operations with Metadata entity
* implements standard hibernate style queries
* */
public interface MetadataRepository extends CrudRepository<Metadata, String> {

}
