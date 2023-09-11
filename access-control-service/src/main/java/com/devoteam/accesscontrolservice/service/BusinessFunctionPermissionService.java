package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.entity.BusinessFunctionPermission;
import com.devoteam.accesscontrolservice.exception.CannotDeleteResourceException;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.repository.BusinessFunctionPermissionRepository;
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
public class BusinessFunctionPermissionService {

  private final BusinessFunctionService businessFunctionService;
  private final PermissionService permissionService;
  private final BusinessFunctionPermissionRepository businessFunctionPermissionRepository;

  public BusinessFunctionPermission createOrRetrieveBusinessFunctionPermission(
      BusinessFunctionPermission businessFunctionPermission) {
    log.info("Received a request to create an association between business function and permission");

    return findByExistingBusinessFunctionPermission(businessFunctionPermission)
        .orElseGet(() -> createNewBusinessFunctionPermission(businessFunctionPermission));
  }

  private Optional<BusinessFunctionPermission> findByExistingBusinessFunctionPermission(
      BusinessFunctionPermission businessFunctionPermission) {
    log.info("Checking if association between business function and permission exists");

    var businessFunction = businessFunctionService.findByIdOrThrowNotFound(
        businessFunctionPermission.getBusinessFunction().getId());
    var permission = permissionService.findByIdOrThrowNotFound(businessFunctionPermission.getPermission().getId());

    return businessFunctionPermissionRepository.findByBusinessFunctionAndPermission(businessFunction, permission);
  }

  private BusinessFunctionPermission createNewBusinessFunctionPermission(
      BusinessFunctionPermission businessFunctionPermission) {
    log.info("Creating new association between business function and permission");

    return businessFunctionPermissionRepository.save(businessFunctionPermission);
  }

  public BusinessFunctionPermission findByIdOrThrowNotFound(UUID businessFunctionPermissionId) {
    log.info("Finding business function permission by ID: '{}'", businessFunctionPermissionId);

    return businessFunctionPermissionRepository.findById(businessFunctionPermissionId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Business function permission with ID: %s not found.".formatted(businessFunctionPermissionId)
        ));
  }

  public Page<BusinessFunctionPermission> listAllByApplicationName(String applicationName, Pageable pageable) {
    log.info("Finding business function permissions by application name: '{}'", applicationName);

    return businessFunctionPermissionRepository.findByBusinessFunction_ApplicationName(applicationName, pageable);
  }

  public void deleteById(UUID id) {
    findByIdOrThrowNotFound(id);
    assertBusinessFunctionPermissionIsNotAssociated(id);

    log.info("Deleting business function permission with ID: '{}'", id);
    businessFunctionPermissionRepository.deleteById(id);
  }

  private void assertBusinessFunctionPermissionIsNotAssociated(UUID id) {
    log.info("Checking for association between business function permission and profile");
    businessFunctionPermissionRepository.findBusinessFunctionPermissionWithProfileById(id)
        .ifPresent(businessFunctionPermission -> {
          throw new CannotDeleteResourceException(
              "Business function permission could not be deleted: it is associated with a profile");
        });
  }

  @Transactional
  public void cascadeDeleteById(UUID id) {
    findByIdOrThrowNotFound(id);

    log.info("Deleting association between profile and business function permission");
    businessFunctionPermissionRepository.deleteProfileBusinessFunctionPermissionsByBusinessFunctionPermissionId(id);

    log.info("Deleting business function permission");
    businessFunctionPermissionRepository.deleteById(id);
  }
}