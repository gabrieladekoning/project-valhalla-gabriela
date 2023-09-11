package com.devoteam.accesscontrolservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.devoteam.accesscontrolservice.entity.Permission;
import com.devoteam.accesscontrolservice.exception.CannotDeleteResourceException;
import com.devoteam.accesscontrolservice.exception.DuplicateFieldException;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.repository.PermissionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class PermissionServiceTest {

  @Mock
  private PermissionRepository permissionRepository;
  @InjectMocks
  PermissionService permissionService;
  private Permission permission;
  private Permission secondPermission;

  @BeforeEach
  public void setup() {
    permission = Permission.builder()
        .id(UUID.randomUUID())
        .name("Read")
        .build();
    secondPermission = Permission.builder()
        .id(UUID.randomUUID())
        .name("Write")
        .build();
  }

  @DisplayName("Saving a new permission successfully")
  @Test
  void createOrRetrievePermission_SuccessfullySaved_WhenFunctionDoesNotAlreadyExist() {
    given(permissionRepository.findByName(permission.getName())).willReturn(Optional.empty());
    given(permissionRepository.save(any())).willReturn(permission);

    var returnedPermission = permissionService.createOrRetrievePermission(permission);

    assertThat(returnedPermission).hasNoNullFieldsOrProperties();
    assertThat(returnedPermission.getId()).isEqualTo(permission.getId());
  }

  @DisplayName("Retrieving an existing permission successfully")
  @Test
  void createOrRetrievePermission_SuccessfullyRetrieved_WhenFunctionAlreadyExists() {
    given(permissionRepository.findByName(permission.getName())).willReturn(Optional.of(permission));

    var returnedPermission = permissionService.createOrRetrievePermission(permission);

    assertThat(returnedPermission).isNotNull();
    assertThat(returnedPermission.getId()).isEqualTo(permission.getId());
  }

  @DisplayName("Handling when permission is not found")
  @Test
  void findByIdOrThrowNotFound_ThrowsResourceNotFoundException_WhenPermissionNotFound() {
    given(permissionRepository.findById(permission.getId())).willReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class,
        () -> permissionService.findByIdOrThrowNotFound(permission.getId()));
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

  @DisplayName("Updating a permission successfully")
  @Test
  void update_SuccessfullyUpdated_WhenPermissionExists() {
    var updatedPermission = Permission.builder()
        .id(permission.getId())
        .name("Delete")
        .build();

    given(permissionRepository.findById(permission.getId())).willReturn(Optional.of(permission));

    permissionService.update(updatedPermission);

    verify(permissionRepository).findById(permission.getId());
    verify(permissionRepository).save(any(Permission.class));
  }

  @DisplayName("Handling when permission is not found during update")
  @Test
  void update_ThrowsResourceNotFoundException_WhenPermissionNotFound() {
    var nonExistentPermission = Permission.builder()
        .id(UUID.randomUUID())
        .name("Does not exist")
        .build();

    given(permissionRepository.findById(nonExistentPermission.getId())).willReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> permissionService.update(nonExistentPermission));
  }

  @DisplayName("Handling when permission name already exists during update")
  @Test
  void update_ThrowsDuplicateFieldException_WhenPermissionNameAlreadyExists() {
    var updatedPermission = Permission.builder()
        .id(permission.getId())
        .name("Delete")
        .build();
    var existingPermission = Permission.builder()
        .id(UUID.randomUUID())
        .name("Delete")
        .build();

    given(permissionRepository.findById(permission.getId())).willReturn(Optional.of(permission));
    given(permissionRepository.findByName(updatedPermission.getName())).willReturn(Optional.of(existingPermission));

    assertThrows(DuplicateFieldException.class, () -> permissionService.update(updatedPermission));
  }

  @DisplayName("Deleting a permission successfully")
  @Test
  void deleteById_SuccessfullyDeleted_WhenNoAssociationsExist() {
    UUID permissionId = UUID.randomUUID();
    Permission permission = new Permission();
    permission.setId(permissionId);

    when(permissionRepository.findById(permissionId)).thenReturn(Optional.of(permission));
    when(permissionRepository.findPermissionWithBusinessFunctionPermissionById(permissionId)).thenReturn(
        Optional.empty());

    permissionService.deleteById(permissionId);

    verify(permissionRepository).deleteById(permissionId);
  }

  @DisplayName("Throwing CannotDeleteResourceException when associations exist")
  @Test
  void deleteById_ThrowsCannotDeleteResourceException_WhenAssociationsExist() {
    UUID permissionId = UUID.randomUUID();
    Permission permission = new Permission();
    permission.setId(permissionId);

    when(permissionRepository.findById(permissionId)).thenReturn(Optional.of(permission));
    when(permissionRepository.findPermissionWithBusinessFunctionPermissionById(permissionId)).thenReturn(
        Optional.of(permission));

    assertThrows(CannotDeleteResourceException.class, () -> permissionService.deleteById(permissionId));
  }

  @DisplayName("Throwing ResourceNotFoundException when permission does not exist")
  @Test
  void deleteById_ThrowsResourceNotFoundException_WhenPermissionNotFound() {
    UUID permissionId = UUID.randomUUID();

    when(permissionRepository.findById(permissionId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> permissionService.deleteById(permissionId));
  }

  @DisplayName("Cascade delete permission successfully when ID exists")
  @Test
  void cascadeDeleteById_SuccessfullyDeleted_WhenIdExists() {
    given(permissionRepository.findById(permission.getId())).willReturn(Optional.of(permission));

    permissionService.cascadeDeleteById(permission.getId());

    verify(permissionRepository).deleteBusinessFunctionPermissionsByPermissionId(permission.getId());
    verify(permissionRepository).deleteProfileBusinessFunctionPermissionsByPermissionId(permission.getId());
    verify(permissionRepository).deleteById(permission.getId());
  }

  @DisplayName("Throws ResourceNotFoundException when ID not found during cascade delete")
  @Test
  void cascadeDeleteById_ThrowsResourceNotFoundException_WhenIdNotFound() {
    given(permissionRepository.findById(permission.getId())).willReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class,
        () -> permissionService.cascadeDeleteById(permission.getId()));
  }

}