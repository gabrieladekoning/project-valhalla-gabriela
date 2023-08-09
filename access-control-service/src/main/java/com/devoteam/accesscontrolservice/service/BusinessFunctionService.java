package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.BusinessFunctionCreationRequest;
import com.devoteam.accesscontrolservice.entity.BusinessFunction;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.mapper.BusinessFunctionMapper;
import com.devoteam.accesscontrolservice.repository.BusinessFunctionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessFunctionService {
    private final BusinessFunctionRepository businessFunctionRepository;

    public BusinessFunction createOrRetrieveBusinessFunction(BusinessFunctionCreationRequest creationRequest) {
        log.info("Received a request to create a new business function with application name: {} and function name: {}",
                creationRequest.getApplicationName(), creationRequest.getFunctionName());

        return findExistingBusinessFunction(creationRequest)
                .orElseGet(() -> createNewBusinessFunction(creationRequest));
    }

    private Optional<BusinessFunction> findExistingBusinessFunction(BusinessFunctionCreationRequest creationRequest) {
        var existingBusinessFunction = businessFunctionRepository.findByApplicationNameAndFunctionName(
                creationRequest.getApplicationName(), creationRequest.getFunctionName());
        existingBusinessFunction.ifPresent(businessFunction ->
                log.info("A business function with application name: {} and function name: {} already exists, returning the existing business function",
                        creationRequest.getApplicationName(), creationRequest.getFunctionName())
        );
        return existingBusinessFunction;
    }

    private BusinessFunction createNewBusinessFunction(BusinessFunctionCreationRequest creationRequest) {
        var businessFunction = BusinessFunctionMapper.INSTANCE.mapToBusinessFunction(creationRequest);
        return businessFunctionRepository.save(businessFunction);
    }

    public BusinessFunction findByIdOrThrowNotFound(String businessFunctionId) {
        return businessFunctionRepository.findById(UUID.fromString(businessFunctionId))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Business function with ID: %s not found.".formatted(businessFunctionId)
                ));
    }
}