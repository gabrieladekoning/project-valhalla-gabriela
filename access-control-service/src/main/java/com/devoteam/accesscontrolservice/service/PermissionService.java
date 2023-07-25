package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.PermissionCreationRequest;
import com.devoteam.accesscontrolservice.dto.PermissionCreationResponse;
import com.devoteam.accesscontrolservice.entity.Permission;
import com.devoteam.accesscontrolservice.mapper.PermissionMapper;
import com.devoteam.accesscontrolservice.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionCreationResponse createOrRetrievePermission(PermissionCreationRequest creationRequest) {
        log.info("Received a request to create a new permission with name: {}", creationRequest.getName());

        var existingPermission = findExistingPermission(creationRequest);

        return existingPermission
                .map(PermissionMapper.INSTANCE::mapToPermissionResponse)
                .orElseGet(() -> createNewPermission(creationRequest));
    }

    private Optional<Permission> findExistingPermission(PermissionCreationRequest creationRequest) {
        var existingPermission = permissionRepository.findByName(creationRequest.getName());
        existingPermission.ifPresent(permission ->
                log.info("A permission with name: {} already exists, returning the existing permission",
                        creationRequest.getName())
        );
        return existingPermission;
    }

    private PermissionCreationResponse createNewPermission(PermissionCreationRequest creationRequest) {
        var permission = PermissionMapper.INSTANCE.mapToPermission(creationRequest);
        var savedPermission = permissionRepository.save(permission);

        log.debug("Created a new permission with ID: {}", savedPermission.getPermissionId());
        return PermissionMapper.INSTANCE.mapToPermissionResponse(savedPermission);
    }
}