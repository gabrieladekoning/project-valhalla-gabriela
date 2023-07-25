package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.BusinessFunctionPermissionCreationRequest;
import com.devoteam.accesscontrolservice.dto.BusinessFunctionPermissionCreationResponse;
import com.devoteam.accesscontrolservice.entity.BusinessFunction;
import com.devoteam.accesscontrolservice.entity.BusinessFunctionPermission;
import com.devoteam.accesscontrolservice.entity.Permission;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.repository.BusinessFunctionPermissionRepository;
import com.devoteam.accesscontrolservice.repository.BusinessFunctionRepository;
import com.devoteam.accesscontrolservice.repository.PermissionRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BusinessFunctionPermissionServiceTest {
    @Mock
    private BusinessFunctionRepository businessFunctionRepository;

    @Mock
    private PermissionRepository permissionRepository;

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
                .businessFunctionId(businessFunctionId)
                .applicationName("Application name")
                .functionName("Function name")
                .build();

        permission = Permission.builder()
                .permissionId(permissionId)
                .name("Permission name")
                .build();

        businessFunctionPermission = BusinessFunctionPermission.builder()
                .businessFunctionPermissionId(UUID.randomUUID())
                .businessFunction(businessFunction)
                .permission(permission)
                .build();
    }

    @DisplayName("Test saving a new BusinessFunctionPermission successfully")
    @Test
    void createOrRetrieveBusinessFunctionPermission_SuccessfullySaved_WhenAssociationDoesNotAlreadyExist() {
        given(businessFunctionRepository.findById(businessFunction.getBusinessFunctionId())).willReturn(Optional.of(businessFunction));
        given(permissionRepository.findById(permission.getPermissionId())).willReturn(Optional.of(permission));
        given(businessFunctionPermissionRepository.findByBusinessFunctionAndPermission(businessFunction, permission)).willReturn(Optional.empty());
        given(businessFunctionPermissionRepository.save(ArgumentMatchers.any())).willReturn(businessFunctionPermission);

        BusinessFunctionPermissionCreationResponse savedBusinessFunctionPermission = businessFunctionPermissionService.createOrRetrieveBusinessFunctionPermission(creationRequest);

        Assertions.assertThat(savedBusinessFunctionPermission).hasNoNullFieldsOrProperties();
        Assertions.assertThat(savedBusinessFunctionPermission.getBusinessFunctionPermissionId()).isEqualTo(businessFunctionPermission.getBusinessFunctionPermissionId().toString());
    }

    @DisplayName("Test retrieving an existing BusinessFunctionPermission successfully")
    @Test
    void createOrRetrieveBusinessFunctionPermission_SuccessfullyRetrieved_WhenAssociationAlreadyExists() {
        given(businessFunctionRepository.findById(businessFunction.getBusinessFunctionId())).willReturn(Optional.of(businessFunction));
        given(permissionRepository.findById(permission.getPermissionId())).willReturn(Optional.of(permission));
        given(businessFunctionPermissionRepository.findByBusinessFunctionAndPermission(businessFunction, permission)).willReturn(Optional.of(businessFunctionPermission));

        BusinessFunctionPermissionCreationResponse savedBusinessFunctionPermission = businessFunctionPermissionService.createOrRetrieveBusinessFunctionPermission(creationRequest);

        Assertions.assertThat(savedBusinessFunctionPermission).isNotNull();
        Assertions.assertThat(savedBusinessFunctionPermission.getBusinessFunctionPermissionId()).isEqualTo(businessFunctionPermission.getBusinessFunctionPermissionId().toString());
    }

    @DisplayName("Test handling when BusinessFunction is not found")
    @Test
    void createOrRetrieveBusinessFunctionPermission_ThrowsResourceNotFoundException_WhenBusinessFunctionNotFound() {
        given(businessFunctionRepository.findById(businessFunction.getBusinessFunctionId())).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> businessFunctionPermissionService.createOrRetrieveBusinessFunctionPermission(creationRequest));
    }

    @DisplayName("Test handling when Permission is not found")
    @Test
    void createOrRetrieveBusinessFunctionPermission_ThrowsResourceNotFoundException_WhenPermissionNotFound() {
        given(businessFunctionRepository.findById(businessFunction.getBusinessFunctionId())).willReturn(Optional.of(businessFunction));
        given(permissionRepository.findById(permission.getPermissionId())).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> businessFunctionPermissionService.createOrRetrieveBusinessFunctionPermission(creationRequest));
    }
}