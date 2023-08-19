package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.PermissionCreationRequest;
import com.devoteam.accesscontrolservice.entity.Permission;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.mapper.PermissionMapper;
import com.devoteam.accesscontrolservice.repository.PermissionRepository;
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
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public Permission createOrRetrievePermission(PermissionCreationRequest creationRequest) {
        log.info("Received a request to create a new permission with name: '{}'", creationRequest.getName());

        return findExistingPermission(creationRequest)
                .orElseGet(() -> createNewPermission(creationRequest));
    }

    private Optional<Permission> findExistingPermission(PermissionCreationRequest creationRequest) {
        log.info("Checking if permission exists '{}'", creationRequest);

        return permissionRepository.findByName(creationRequest.getName());
    }

    private Permission createNewPermission(PermissionCreationRequest creationRequest) {
        log.info("Creating new permission '{}'", creationRequest);

        var permission = permissionMapper.mapToPermission(creationRequest);

        return permissionRepository.save(permission);
    }

    public Permission findByIdOrThrowNotFound(String permissionId) {
        log.info("Finding permission by ID: '{}'", permissionId);

        return permissionRepository.findById(UUID.fromString(permissionId))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Permission with ID: %s not found.".formatted(permissionId)
                ));
    }

    public Page<Permission> listAll(Pageable pageable) {
        log.info("Finding all permissions");

        return permissionRepository.findAll(pageable);
    }
}