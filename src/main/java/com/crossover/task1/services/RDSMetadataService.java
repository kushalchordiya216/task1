package com.crossover.task1.services;

import com.crossover.task1.entities.Metadata;
import com.crossover.task1.interfaces.IMetadataService;
import com.crossover.task1.repository.MetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the IMetadataService for an AWS RDS database
 * Has a repository dependency ban injected into it.
 */
@Service
public class RDSMetadataService implements IMetadataService {
    @Autowired
    MetadataRepository repository;

    /**
     * Takes in the various values that consist the file metadata and constructs a metadata entity out of it
     * Stores it to RDS using the injected repository
     * @param objKey The unique objKey used to store the object o a storage platform like S3
     * @param description description of the file provided by the client
     * @param filetype contentType of the file, eg. image/jpeg
     * @param size size of the file in bytes
     * @return Metadata entity constructed from the given metadata values
     */
    @Override
    @Transactional
    public Metadata save(String objKey, String description, String filetype, Long size) {
        Metadata metadata = new Metadata(objKey, description, filetype, size);
        return repository.save(metadata);
    }

    public List<Metadata> fetchAll(){
        List<Metadata> metadata = new ArrayList<>();
        repository.findAll().forEach(metadata::add);
        return metadata;
    }
}
