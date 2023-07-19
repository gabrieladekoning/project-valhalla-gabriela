package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.BusinessFunctionPermissionCreationRequest;
import com.devoteam.accesscontrolservice.dto.BusinessFunctionPermissionCreationResponse;
import com.devoteam.accesscontrolservice.entity.BusinessFunctionPermission;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.mapper.BusinessFunctionPermissionMapper;
import com.devoteam.accesscontrolservice.repository.BusinessFunctionPermissionRepository;
import com.devoteam.accesscontrolservice.repository.BusinessFunctionRepository;
import com.devoteam.accesscontrolservice.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessFunctionPermissionService {
    private final BusinessFunctionRepository businessFunctionRepository;
    private final PermissionRepository permissionRepository;
    private final BusinessFunctionPermissionRepository businessFunctionPermissionRepository;

    public BusinessFunctionPermissionCreationResponse createOrRetrieveBusinessFunctionPermission(BusinessFunctionPermissionCreationRequest creationRequest) {
        log.debug("Received a request to create an association between BusinessFunction ID: {} and Permission ID: {}",
                creationRequest.getBusinessFunctionId(), creationRequest.getPermissionId());

        var existingAssociation = findByExistingBusinessFunctionPermission(creationRequest);

        return existingAssociation
                .map(BusinessFunctionPermissionMapper.INSTANCE::mapToBusinessFunctionPermissionResponse)
                .orElseGet(() -> createNewBusinessFunctionPermission(creationRequest));
    }

    private Optional<BusinessFunctionPermission> findByExistingBusinessFunctionPermission(BusinessFunctionPermissionCreationRequest creationRequest) {
        var businessFunctionId = UUID.fromString(creationRequest.getBusinessFunctionId());
        var permissionId = creationRequest.getPermissionId();

        var businessFunction = businessFunctionRepository.findById(businessFunctionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "BusinessFunction with ID: %s not found.".formatted(businessFunctionId)
                ));

        var permission = permissionRepository.findById(UUID.fromString(permissionId))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Permission with ID: %s not found.".formatted(permissionId)
                ));
        return businessFunctionPermissionRepository.findByBusinessFunctionAndPermission(businessFunction, permission);
    }

    private BusinessFunctionPermissionCreationResponse createNewBusinessFunctionPermission(BusinessFunctionPermissionCreationRequest creationRequest) {
        var businessFunctionPermission = BusinessFunctionPermissionMapper.INSTANCE.mapToBusinessFunctionPermission(creationRequest);
        var savedBusinessFunctionPermission = businessFunctionPermissionRepository.save(businessFunctionPermission);

        log.debug("Created a new permission with ID: {}", savedBusinessFunctionPermission.getId());
        return BusinessFunctionPermissionMapper.INSTANCE.mapToBusinessFunctionPermissionResponse(savedBusinessFunctionPermission);
    }
}