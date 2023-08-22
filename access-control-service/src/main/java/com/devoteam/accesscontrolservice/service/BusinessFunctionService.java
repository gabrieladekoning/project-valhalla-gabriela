package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.entity.BusinessFunction;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.repository.BusinessFunctionRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessFunctionService {

  private final BusinessFunctionRepository businessFunctionRepository;

  public BusinessFunction createOrRetrieveBusinessFunction(BusinessFunction businessFunction) {
    log.info("Received a request to create a new business function with application name: '{}' and function name: '{}'",
        businessFunction.getApplicationName(), businessFunction.getFunctionName());

    return findExistingBusinessFunction(businessFunction)
        .orElseGet(() -> createNewBusinessFunction(businessFunction));
  }

  private Optional<BusinessFunction> findExistingBusinessFunction(BusinessFunction businessFunction) {
    log.info("Checking if business function exists '{}'", businessFunction);

    return businessFunctionRepository.findByApplicationNameAndFunctionName(
        businessFunction.getApplicationName(), businessFunction.getFunctionName());
  }

  private BusinessFunction createNewBusinessFunction(BusinessFunction businessFunction) {
    log.info("Creating new business function '{}'", businessFunction);

    return businessFunctionRepository.save(businessFunction);
  }

  public BusinessFunction findByIdOrThrowNotFound(UUID businessFunctionId) {
    log.info("Finding business function by ID: '{}'", businessFunctionId);

    return businessFunctionRepository.findById(businessFunctionId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Business function with ID: %s not found.".formatted(businessFunctionId)
        ));
  }

  public Page<BusinessFunction> listAll(Pageable pageable) {
    log.info("Finding all businessFunctions");

    return businessFunctionRepository.findAll(pageable);
  }
}