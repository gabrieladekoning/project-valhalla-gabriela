package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.BusinessFunctionCreationRequest;
import com.devoteam.accesscontrolservice.entity.BusinessFunction;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.mapper.BusinessFunctionMapper;
import com.devoteam.accesscontrolservice.repository.BusinessFunctionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessFunctionService {
    private final BusinessFunctionRepository businessFunctionRepository;

    public BusinessFunction createOrRetrieveBusinessFunction(BusinessFunctionCreationRequest creationRequest) {
        log.info("Received a request to create a new business function with application name: '{}' and function name: '{}'",
                creationRequest.getApplicationName(), creationRequest.getFunctionName());

        return findExistingBusinessFunction(creationRequest)
                .orElseGet(() -> createNewBusinessFunction(creationRequest));
    }

    private Optional<BusinessFunction> findExistingBusinessFunction(BusinessFunctionCreationRequest creationRequest) {
        log.info("Checking if business function exists '{}'", creationRequest);

        return businessFunctionRepository.findByApplicationNameAndFunctionName(
                creationRequest.getApplicationName(), creationRequest.getFunctionName());
    }

    private BusinessFunction createNewBusinessFunction(BusinessFunctionCreationRequest creationRequest) {
        log.info("Creating new business function '{}'", creationRequest);

        var businessFunction = BusinessFunctionMapper.INSTANCE.mapToBusinessFunction(creationRequest);

        return businessFunctionRepository.save(businessFunction);
    }

    public BusinessFunction findByIdOrThrowNotFound(String businessFunctionId) {
        log.info("Finding business function by ID: '{}'", businessFunctionId);

        return businessFunctionRepository.findById(UUID.fromString(businessFunctionId))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Business function with ID: %s not found.".formatted(businessFunctionId)
                ));
    }

    public Page<BusinessFunction> listAll(Pageable pageable) {
        log.info("Finding all businessFunctions");

        return businessFunctionRepository.findAll(pageable);
    }
}