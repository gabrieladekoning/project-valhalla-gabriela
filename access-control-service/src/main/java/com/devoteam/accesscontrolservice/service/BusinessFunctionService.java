package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.entity.BusinessFunction;
import com.devoteam.accesscontrolservice.exception.CannotDeleteResourceException;
import com.devoteam.accesscontrolservice.exception.DuplicateFieldException;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.repository.BusinessFunctionRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  public void update(BusinessFunction businessFunction) {
    var businessFunctionToUpdate = findByIdOrThrowNotFound(businessFunction.getId());

    assertApplicationAndFunctionNameDoNotExist(businessFunction.getApplicationName(),
        businessFunction.getFunctionName(), businessFunction.getId());

    log.info("Updating application name from '{}' to '{}'", businessFunctionToUpdate.getApplicationName(),
        businessFunction.getApplicationName());
    businessFunctionToUpdate.setApplicationName(businessFunction.getApplicationName());
    log.info("Updating function name from '{}' to '{}'", businessFunctionToUpdate.getFunctionName(),
        businessFunction.getFunctionName());
    businessFunctionToUpdate.setFunctionName(businessFunction.getFunctionName());

    businessFunctionRepository.save(businessFunctionToUpdate);
  }

  private void assertApplicationAndFunctionNameDoNotExist(String applicationName, String functionName, UUID id) {
    log.info("Checking if application name and function name combination exists");
    businessFunctionRepository.findByApplicationNameAndFunctionName(applicationName, functionName)
        .filter(businessFunction -> !businessFunction.getId().equals(id))
        .ifPresent(businessFunction -> {
          throw new DuplicateFieldException(
              "Combination of application name and function name is already registered: '%s', '%s'".formatted(
                  businessFunction.getApplicationName(), businessFunction.getFunctionName()));
        });
  }

  public void deleteById(UUID id) {
    findByIdOrThrowNotFound(id);
    assertBusinessFunctionIsNotAssociated(id);

    log.info("Deleting business function with ID: '{}'", id);
    businessFunctionRepository.deleteById(id);
  }

  private void assertBusinessFunctionIsNotAssociated(UUID id) {
    log.info("Checking for association between business function and permission");
    businessFunctionRepository.findBusinessFunctionWithPermissionById(id)
        .ifPresent(businessFunction -> {
          throw new CannotDeleteResourceException(
              "Business function could not be deleted: it is associated with a permission");
        });
  }

  @Transactional
  public void cascadeDeleteById(UUID id) {
    findByIdOrThrowNotFound(id);

    log.info("Deleting association between profile and business function permission");
    businessFunctionRepository.deleteProfileBusinessFunctionPermissionsByBusinessFunctionId(id);

    log.info("Deleting association between business function and permission");
    businessFunctionRepository.deleteBusinessFunctionPermissionsByBusinessFunctionId(id);

    log.info("Deleting business function");
    businessFunctionRepository.deleteById(id);
  }
}