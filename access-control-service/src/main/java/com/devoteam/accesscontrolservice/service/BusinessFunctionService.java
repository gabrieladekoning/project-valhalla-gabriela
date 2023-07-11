package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.BusinessFunctionCreationRequest;
import com.devoteam.accesscontrolservice.dto.BusinessFunctionCreationResponse;
import com.devoteam.accesscontrolservice.entity.BusinessFunction;
import com.devoteam.accesscontrolservice.mapper.BusinessFunctionMapper;
import com.devoteam.accesscontrolservice.repository.BusinessFunctionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessFunctionService {
    private final BusinessFunctionRepository businessFunctionRepository;
    private final BusinessFunctionMapper businessFunctionMapper;

    public BusinessFunctionCreationResponse createOrRetrieveBusinessFunction(BusinessFunctionCreationRequest creationRequest) {
        log.info("Received a request to create a new business function with applicationName: {} and functionName: {}",
                creationRequest.getApplicationName(), creationRequest.getFunctionName());

        Optional<BusinessFunction> existingBusinessFunction = findExistingBusinessFunction(creationRequest);

        return existingBusinessFunction
                .map(businessFunctionMapper::mapToBusinessFunctionResponse)
                .orElseGet(() -> createNewBusinessFunction(creationRequest));
    }

    private BusinessFunctionCreationResponse createNewBusinessFunction(BusinessFunctionCreationRequest creationRequest) {
        var businessFunction = businessFunctionMapper.mapToBusinessFunction(creationRequest);
        var savedBusinessFunction = businessFunctionRepository.save(businessFunction);

        log.debug("Created a new business function with ID: {}", savedBusinessFunction.getId());
        return businessFunctionMapper.mapToBusinessFunctionResponse(savedBusinessFunction);
    }

    private Optional<BusinessFunction> findExistingBusinessFunction(BusinessFunctionCreationRequest creationRequest) {
        var existingBusinessFunction = businessFunctionRepository.findByApplicationNameAndFunctionName(
                creationRequest.getApplicationName(), creationRequest.getFunctionName());
        existingBusinessFunction.ifPresent(businessFunction ->
                log.info("A business function with applicationName: {} and functionName: {} already exists, returning the existing businessFunction",
                        creationRequest.getApplicationName(), creationRequest.getFunctionName())
        );
        return existingBusinessFunction;
    }
}