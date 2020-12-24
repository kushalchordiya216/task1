package com.crossover.task1.interfaces;

import com.crossover.task1.entities.Metadata;
import com.crossover.task1.exceptions.InternalException;

/*
* Interface to save metadata of files being uploaded
* Interface should be implemented by specific services to interact with specific data stores
* */
public interface IMetadataService {
    Metadata save(String objKey, String description, String filetype, Long size) throws InternalException;
}
