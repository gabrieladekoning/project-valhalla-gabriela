package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.BusinessFunctionPermissionCreationRequest;
import com.devoteam.accesscontrolservice.entity.BusinessFunctionPermission;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.mapper.BusinessFunctionPermissionMapper;
import com.devoteam.accesscontrolservice.repository.BusinessFunctionPermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessFunctionPermissionService {
    private final BusinessFunctionService businessFunctionService;
    private final PermissionService permissionService;
    private final BusinessFunctionPermissionRepository businessFunctionPermissionRepository;

    public BusinessFunctionPermission createOrRetrieveBusinessFunctionPermission(BusinessFunctionPermissionCreationRequest creationRequest) {
        log.debug("Received a request to create an association between business function ID: {} and permission ID: {}",
                creationRequest.getBusinessFunctionId(), creationRequest.getPermissionId());

        return findByExistingBusinessFunctionPermission(creationRequest)
                .orElseGet(() -> createNewBusinessFunctionPermission(creationRequest));
    }

    private Optional<BusinessFunctionPermission> findByExistingBusinessFunctionPermission(BusinessFunctionPermissionCreationRequest creationRequest) {
        var businessFunction = businessFunctionService.findByIdOrThrowNotFound(creationRequest.getBusinessFunctionId());
        var permission = permissionService.findByIdOrThrowNotFound(creationRequest.getPermissionId());
        return businessFunctionPermissionRepository.findByBusinessFunctionAndPermission(businessFunction, permission);
    }

    private BusinessFunctionPermission createNewBusinessFunctionPermission(BusinessFunctionPermissionCreationRequest creationRequest) {
        var businessFunctionPermission = BusinessFunctionPermissionMapper.INSTANCE.mapToBusinessFunctionPermission(creationRequest);
        var savedBusinessFunctionPermission = businessFunctionPermissionRepository.save(businessFunctionPermission);

        log.debug("Created a new association between business function and permission with ID: '{}'", savedBusinessFunctionPermission.getId());
        return savedBusinessFunctionPermission;
    }

    public BusinessFunctionPermission findByIdOrThrowNotFound(String businessFunctionPermissionId) {
        return businessFunctionPermissionRepository.findById(UUID.fromString(businessFunctionPermissionId))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Business function permission with ID: %s not found.".formatted(businessFunctionPermissionId)
                ));
    }
}