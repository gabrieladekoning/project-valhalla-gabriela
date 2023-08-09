package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.PermissionCreationRequest;
import com.devoteam.accesscontrolservice.entity.Permission;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.repository.PermissionRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PermissionServiceTest {
    @Mock
    PermissionRepository permissionRepository;
    @InjectMocks
    PermissionService permissionService;
    private PermissionCreationRequest creationRequest;
    private Permission permission;

    @BeforeEach
    public void setup() {
        UUID uuid = UUID.randomUUID();
        creationRequest = PermissionCreationRequest.builder()
                .name("Permission name")
                .build();
        permission = Permission.builder()
                .id(uuid)
                .name(creationRequest.getName())
                .build();
    }

    @DisplayName("Saving a new permission successfully")
    @Test
    void createOrRetrievePermission_SuccessfullySaved_WhenFunctionDoesNotAlreadyExist() {
        given(permissionRepository.findByName(creationRequest.getName())).willReturn(Optional.empty());
        given(permissionRepository.save(ArgumentMatchers.any())).willReturn(permission);

        var returnedPermission = permissionService.createOrRetrievePermission(creationRequest);

        assertThat(returnedPermission).hasNoNullFieldsOrProperties();
        assertThat(returnedPermission.getId()).isEqualTo(permission.getId());
    }

    @DisplayName("Retrieving an existing permission successfully")
    @Test
    void createOrRetrievePermission_SuccessfullyRetrieved_WhenFunctionAlreadyExists() {
        given(permissionRepository.findByName(creationRequest.getName())).willReturn(Optional.of(permission));

        var returnedPermission = permissionService.createOrRetrievePermission(creationRequest);

        assertThat(returnedPermission).isNotNull();
        assertThat(returnedPermission.getId()).isEqualTo(permission.getId());
    }

    @DisplayName("Handling when permission is not found")
    @Test
    void findByIdOrThrowNotFound_ThrowsResourceNotFoundException_WhenPermissionNotFound() {
        given(permissionRepository.findById(permission.getId())).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> permissionService.findByIdOrThrowNotFound(String.valueOf(permission.getId())));
    }
}