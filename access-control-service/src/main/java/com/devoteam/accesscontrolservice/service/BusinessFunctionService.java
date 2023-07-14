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

    public BusinessFunctionCreationResponse createOrRetrieveBusinessFunction(BusinessFunctionCreationRequest creationRequest) {
        log.info("Received a request to create a new business function with applicationName: {} and functionName: {}",
                creationRequest.getApplicationName(), creationRequest.getFunctionName());

        var existingBusinessFunction = findExistingBusinessFunction(creationRequest);

        return existingBusinessFunction
                .map(BusinessFunctionMapper.INSTANCE::mapToBusinessFunctionResponse)
                .orElseGet(() -> createNewBusinessFunction(creationRequest));
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

    private BusinessFunctionCreationResponse createNewBusinessFunction(BusinessFunctionCreationRequest creationRequest) {
        var businessFunction = BusinessFunctionMapper.INSTANCE.mapToBusinessFunction(creationRequest);
        var savedBusinessFunction = businessFunctionRepository.save(businessFunction);

        log.debug("Created a new business function with ID: {}", savedBusinessFunction.getId());
        return BusinessFunctionMapper.INSTANCE.mapToBusinessFunctionResponse(savedBusinessFunction);
    }
}