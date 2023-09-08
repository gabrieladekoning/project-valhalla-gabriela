package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.entity.Permission;
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

  public Permission findByIdOrThrowNotFound(UUID permissionId) {
    log.info("Finding permission by ID: '{}'", permissionId);

    return permissionRepository.findById(permissionId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Permission with ID: %s not found.".formatted(permissionId)
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
}