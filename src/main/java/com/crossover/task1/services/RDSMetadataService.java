package com.crossover.task1.services;

import com.crossover.task1.entities.Metadata;
import com.crossover.task1.interfaces.IMetadataService;
import com.crossover.task1.repository.MetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class RDSMetadataService implements IMetadataService {
    @Autowired
    MetadataRepository repository;

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
