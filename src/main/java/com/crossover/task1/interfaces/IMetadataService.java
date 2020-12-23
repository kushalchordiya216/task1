package com.crossover.task1.interfaces;

import com.crossover.task1.entities.Metadata;
import com.crossover.task1.exceptions.InternalException;

public interface IMetadataService {
    Metadata save(String objKey, String description, String filetype, Long size) throws InternalException;
}
