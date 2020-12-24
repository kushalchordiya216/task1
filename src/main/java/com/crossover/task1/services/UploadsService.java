package com.crossover.task1.services;

import com.crossover.task1.exceptions.InternalException;
import com.crossover.task1.exceptions.ValidationException;
import com.crossover.task1.interfaces.IMediaValidatorService;
import com.crossover.task1.interfaces.IMetadataService;
import com.crossover.task1.interfaces.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Main business service that wraps up all business logic. Consists of three smaller segregated interfaces
 * which are all injected into the service. Validated incoming file, uploads it to storage platform if valid
 * and stores metadata for the same in a specified data store
 */
@Service
public class UploadsService {
    private final IMediaValidatorService mediaValidatorService;
    private final IStorageService storageService;
    private final IMetadataService metaDataService;

    @Autowired
    public UploadsService(IMediaValidatorService mediaValidatorService, IStorageService storageService, IMetadataService metaDataService){
        this.mediaValidatorService = mediaValidatorService;
        this.storageService = storageService;
        this.metaDataService = metaDataService;
    }

    /**
     * Takes in a multipart file, and passes it through the validator, storage and metadata services respectively
     * Manages transactional consistency between storage and metadata, rolling one back if the other fails.
     * @param file Multipart file sent by client
     * @param description description of the file sent by client
     * @return true if all operation succeed
     * @throws ValidationException if file is invalid
     * @throws InternalException if storage or metadata operations fail
     */
    public boolean upload(MultipartFile file, String description) throws ValidationException, InternalException {
        this.mediaValidatorService.validate(file);
        String objKey = this.storageService.store(file,file.getContentType());
        try {
            this.metaDataService.save(objKey, description, file.getContentType(), file.getSize());
        } catch (Exception ex) {
            this.storageService.delete(objKey);
            throw new InternalException("Error while saving metadata to database! Upload has been rolled back");
        }
        return true;
    }

}
