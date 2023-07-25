package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.PermissionCreationRequest;
import com.devoteam.accesscontrolservice.dto.PermissionCreationResponse;
import com.devoteam.accesscontrolservice.entity.Permission;
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
                .permissionId(uuid)
                .name(creationRequest.getName())
                .build();
    }

    @DisplayName("Test saving a new permission successfully")
    @Test
    void createOrRetrievePermission_SuccessfullySaved_WhenFunctionDoesNotAlreadyExist() {
        given(permissionRepository.findByName(creationRequest.getName())).willReturn(Optional.empty());
        given(permissionRepository.save(ArgumentMatchers.any())).willReturn(permission);

        PermissionCreationResponse savedPermissionResponse = permissionService.createOrRetrievePermission(creationRequest);

        Assertions.assertThat(savedPermissionResponse).hasNoNullFieldsOrProperties();
        Assertions.assertThat(savedPermissionResponse.getPermissionId()).isEqualTo(permission.getPermissionId().toString());
    }

    @DisplayName("Test retrieving an existing permission successfully")
    @Test
    void createOrRetrievePermission_SuccessfullyRetrieved_WhenFunctionAlreadyExists() {
        given(permissionRepository.findByName(creationRequest.getName())).willReturn(Optional.of(permission));

        PermissionCreationResponse savedPermissionResponse = permissionService.createOrRetrievePermission(creationRequest);

        Assertions.assertThat(savedPermissionResponse).isNotNull();
        Assertions.assertThat(savedPermissionResponse.getPermissionId()).isEqualTo(permission.getPermissionId().toString());
    }
}