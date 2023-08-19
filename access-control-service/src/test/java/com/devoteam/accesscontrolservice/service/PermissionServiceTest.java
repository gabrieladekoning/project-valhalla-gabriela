package com.devoteam.accesscontrolservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.devoteam.accesscontrolservice.dto.PermissionCreationRequest;
import com.devoteam.accesscontrolservice.entity.Permission;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.mapper.PermissionMapper;
import com.devoteam.accesscontrolservice.repository.PermissionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class PermissionServiceTest {
    @Mock
    private PermissionRepository permissionRepository;
    @Mock
    private PermissionMapper permissionMapper;
    @InjectMocks
    PermissionService permissionService;
    private PermissionCreationRequest creationRequest;
    private Permission permission;
    private Permission secondPermission;

    @BeforeEach
    public void setup() {
        UUID uuid = UUID.randomUUID();
        creationRequest = PermissionCreationRequest.builder()
                .name("Read")
                .build();
        permission = Permission.builder()
                .id(uuid)
                .name(creationRequest.getName())
                .build();
        secondPermission = Permission.builder()
                .id(uuid)
                .name("Write")
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

    @Test
    @DisplayName("Returning a pageable list of permissions when successful")
    void listAll_ReturnListOfPermissionsInsidePageObject_WhenSuccessful() {
        List<Permission> expectedPermissions = new ArrayList<>();
        expectedPermissions.add(permission);
        expectedPermissions.add(secondPermission);

        var pageable = PageRequest.of(0, 5);
        var permissionPage = new PageImpl<>(expectedPermissions, pageable, expectedPermissions.size());

        when(permissionRepository.findAll(pageable)).thenReturn(permissionPage);

        var resultPermissionPage = permissionService.listAll(pageable);

        verify(permissionRepository).findAll(pageable);

        assertThat(resultPermissionPage).isNotNull();
        assertThat(resultPermissionPage.toList()).isNotEmpty();
        assertThat(resultPermissionPage.toList().get(0).getName()).isEqualTo(permission.getName());
        assertThat(resultPermissionPage.toList().get(1).getName()).isEqualTo(secondPermission.getName());
    }
}