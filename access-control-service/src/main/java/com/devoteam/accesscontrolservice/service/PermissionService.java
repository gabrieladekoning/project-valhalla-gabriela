package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.entity.Permission;
import com.devoteam.accesscontrolservice.exception.CannotDeleteResourceException;
import com.devoteam.accesscontrolservice.exception.DuplicateFieldException;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.repository.PermissionRepository;
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
public class PermissionService {

  private final PermissionRepository permissionRepository;

  public Permission createOrRetrievePermission(Permission permission) {
    log.info("Received a request to create a new permission with name: '{}'", permission.getName());

    return findExistingPermission(permission)
        .orElseGet(() -> createNewPermission(permission));
  }

  private Optional<Permission> findExistingPermission(Permission permission) {
    log.info("Checking if permission exists '{}'", permission.getName());

    return permissionRepository.findByName(permission.getName());
  }

  private Permission createNewPermission(Permission permission) {
    log.info("Creating new permission '{}'", permission.getName());

    return permissionRepository.save(permission);
  }

  public Permission findByIdOrThrowNotFound(UUID id) {
    log.info("Finding permission by ID: '{}'", id);

    return permissionRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Permission with ID: %s not found.".formatted(id)
        ));
  }

  public Page<Permission> listAll(Pageable pageable) {
    log.info("Finding all permissions");

    return permissionRepository.findAll(pageable);
  }

  public void update(Permission permission) {
    var permissionToUpdate = findByIdOrThrowNotFound(permission.getId());

    assertNameDoesNotExist(permission.getName(), permission.getId());

    log.info("Updating permission name from '{}' to '{}'", permissionToUpdate.getName(), permission.getName());

    permissionToUpdate.setName(permission.getName());
    permissionRepository.save(permissionToUpdate);
  }

  private void assertNameDoesNotExist(String name, UUID id) {
    log.info("Checking if permission name exists");
    permissionRepository.findByName(name)
        .filter(permission -> !permission.getId().equals(id))
        .ifPresent(permission -> {
          throw new DuplicateFieldException(
              "Permission name is already registered: '%s'".formatted(permission.getName()));
        });
  }

  public void deleteById(UUID id) {
    findByIdOrThrowNotFound(id);
    assertBusinessFunctionIsNotAssociated(id);

    log.info("Deleting permission with ID: '{}'", id);
    permissionRepository.deleteById(id);
  }

  private void assertBusinessFunctionIsNotAssociated(UUID id) {
    log.info("Checking for association between permission and business function");
    permissionRepository.findPermissionWithBusinessFunctionPermissionById(id)
        .ifPresent(permission -> {
          throw new CannotDeleteResourceException(
              "Permission could not be deleted: it is associated with a business function");
        });
  }

  @Transactional
  public void cascadeDeleteById(UUID id) {
    findByIdOrThrowNotFound(id);

    log.info("Deleting association between profile and permission permission");
    permissionRepository.deleteProfileBusinessFunctionPermissionsByPermissionId(id);

    log.info("Deleting association between business function and permission");
    permissionRepository.deleteBusinessFunctionPermissionsByPermissionId(id);

    log.info("Deleting permission");
    permissionRepository.deleteById(id);
  }
}