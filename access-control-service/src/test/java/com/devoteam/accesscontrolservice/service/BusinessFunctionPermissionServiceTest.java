package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.BusinessFunctionPermissionCreationRequest;
import com.devoteam.accesscontrolservice.entity.BusinessFunction;
import com.devoteam.accesscontrolservice.entity.BusinessFunctionPermission;
import com.devoteam.accesscontrolservice.entity.Permission;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.repository.BusinessFunctionPermissionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BusinessFunctionPermissionServiceTest {
    @Mock
    private BusinessFunctionService businessFunctionService;

    @Mock
    private PermissionService permissionService;

    @Mock
    private BusinessFunctionPermissionRepository businessFunctionPermissionRepository;

    @InjectMocks
    private BusinessFunctionPermissionService businessFunctionPermissionService;

    private BusinessFunctionPermissionCreationRequest creationRequest;
    private BusinessFunction businessFunction;
    private Permission permission;
    private BusinessFunctionPermission businessFunctionPermission;

    @BeforeEach
    public void setup() {
        UUID businessFunctionId = UUID.randomUUID();
        UUID permissionId = UUID.randomUUID();

        creationRequest = BusinessFunctionPermissionCreationRequest.builder()
                .businessFunctionId(String.valueOf(businessFunctionId))
                .permissionId(String.valueOf(permissionId))
                .build();

        businessFunction = BusinessFunction.builder()
                .id(businessFunctionId)
                .applicationName("Application name")
                .functionName("Function name")
                .build();

        permission = Permission.builder()
                .id(permissionId)
                .name("Permission name")
                .build();

        businessFunctionPermission = BusinessFunctionPermission.builder()
                .id(UUID.randomUUID())
                .businessFunction(businessFunction)
                .permission(permission)
                .build();
    }

    @DisplayName("Test saving a new BusinessFunctionPermission successfully")
    @Test
    void createOrRetrieveBusinessFunctionPermission_SuccessfullySaved_WhenAssociationDoesNotAlreadyExist() {
        given(businessFunctionService.findByIdOrThrowNotFound(String.valueOf(businessFunction.getId()))).willReturn(businessFunction);
        given(permissionService.findByIdOrThrowNotFound(String.valueOf(permission.getId()))).willReturn(permission);
        given(businessFunctionPermissionRepository.findByBusinessFunctionAndPermission(businessFunction, permission)).willReturn(Optional.empty());
        given(businessFunctionPermissionRepository.save(ArgumentMatchers.any())).willReturn(businessFunctionPermission);

        BusinessFunctionPermission savedBusinessFunctionPermission = businessFunctionPermissionService.createOrRetrieveBusinessFunctionPermission(creationRequest);

        Assertions.assertThat(savedBusinessFunctionPermission).hasNoNullFieldsOrProperties();
        Assertions.assertThat(savedBusinessFunctionPermission.getId()).isEqualTo(businessFunctionPermission.getId());
    }

    @DisplayName("Test retrieving an existing BusinessFunctionPermission successfully")
    @Test
    void createOrRetrieveBusinessFunctionPermission_SuccessfullyRetrieved_WhenAssociationAlreadyExists() {
        given(businessFunctionService.findByIdOrThrowNotFound(String.valueOf(businessFunction.getId()))).willReturn(businessFunction);
        given(permissionService.findByIdOrThrowNotFound(String.valueOf(permission.getId()))).willReturn(permission);
        given(businessFunctionPermissionRepository.findByBusinessFunctionAndPermission(businessFunction, permission)).willReturn(Optional.of(businessFunctionPermission));

        BusinessFunctionPermission retrievedBusinessFunctionPermission = businessFunctionPermissionService.createOrRetrieveBusinessFunctionPermission(creationRequest);

        Assertions.assertThat(retrievedBusinessFunctionPermission).isNotNull();
        Assertions.assertThat(retrievedBusinessFunctionPermission.getId()).isEqualTo(businessFunctionPermission.getId());
    }

    @DisplayName("Test handling when BusinessFunction is not found in findByExistingBusinessFunctionPermission")
    @Test
    void createOrRetrieveBusinessFunctionPermission_ThrowsResourceNotFoundException_WhenBusinessFunctionNotFoundInFind() {
        given(businessFunctionService.findByIdOrThrowNotFound(creationRequest.getBusinessFunctionId())).willThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> businessFunctionPermissionService.createOrRetrieveBusinessFunctionPermission(creationRequest));
    }

    @DisplayName("Test handling when Permission is not found in findByExistingBusinessFunctionPermission")
    @Test
    void createOrRetrieveBusinessFunctionPermission_ThrowsResourceNotFoundException_WhenPermissionNotFoundInFind() {
        given(businessFunctionService.findByIdOrThrowNotFound(creationRequest.getBusinessFunctionId())).willReturn(businessFunction);
        given(permissionService.findByIdOrThrowNotFound(creationRequest.getPermissionId())).willThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> businessFunctionPermissionService.createOrRetrieveBusinessFunctionPermission(creationRequest));
    }

    @DisplayName("Test handling when BusinessFunctionPermission is not found")
    @Test
    void findByIdOrThrowNotFound_ThrowsResourceNotFoundException_WhenBusinessFunctionNotFound() {
        given(businessFunctionPermissionRepository.findById(permission.getId())).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> businessFunctionPermissionService.findByIdOrThrowNotFound(String.valueOf(permission.getId())));
    }
}