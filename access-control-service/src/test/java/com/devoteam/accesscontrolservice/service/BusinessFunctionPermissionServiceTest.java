package com.devoteam.accesscontrolservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.devoteam.accesscontrolservice.entity.BusinessFunction;
import com.devoteam.accesscontrolservice.entity.BusinessFunctionPermission;
import com.devoteam.accesscontrolservice.entity.Permission;
import com.devoteam.accesscontrolservice.exception.CannotDeleteResourceException;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.repository.BusinessFunctionPermissionRepository;
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
class BusinessFunctionPermissionServiceTest {

  @Mock
  private BusinessFunctionService businessFunctionService;
  @Mock
  private PermissionService permissionService;
  @Mock
  private BusinessFunctionPermissionRepository businessFunctionPermissionRepository;
  @InjectMocks
  private BusinessFunctionPermissionService businessFunctionPermissionService;
  private BusinessFunction businessFunction;
  private Permission permission;
  private BusinessFunctionPermission businessFunctionPermission;

  @BeforeEach
  public void setup() {
    businessFunction = BusinessFunction.builder()
        .id(UUID.randomUUID())
        .applicationName("Application name")
        .functionName("Function name")
        .build();
    permission = Permission.builder()
        .id(UUID.randomUUID())
        .name("Permission name")
        .build();

    businessFunctionPermission = BusinessFunctionPermission.builder()
        .id(UUID.randomUUID())
        .businessFunction(businessFunction)
        .permission(permission)
        .build();
  }

  @DisplayName("Saving a new BusinessFunctionPermission successfully")
  @Test
  void createOrRetrieveBusinessFunctionPermission_SuccessfullySaved_WhenAssociationDoesNotAlreadyExist() {
    given(businessFunctionService.findByIdOrThrowNotFound(businessFunction.getId())).willReturn(businessFunction);
    given(permissionService.findByIdOrThrowNotFound(permission.getId())).willReturn(permission);
    given(businessFunctionPermissionRepository.findByBusinessFunctionAndPermission(businessFunction,
        permission)).willReturn(Optional.empty());
    given(businessFunctionPermissionRepository.save(ArgumentMatchers.any())).willReturn(businessFunctionPermission);

    var savedBusinessFunctionPermission = businessFunctionPermissionService.createOrRetrieveBusinessFunctionPermission(
        businessFunctionPermission);

    assertThat(savedBusinessFunctionPermission).hasNoNullFieldsOrProperties();
    assertThat(savedBusinessFunctionPermission.getId()).isEqualTo(businessFunctionPermission.getId());
  }

  @DisplayName("Retrieving an existing BusinessFunctionPermission successfully")
  @Test
  void createOrRetrieveBusinessFunctionPermission_SuccessfullyRetrieved_WhenAssociationAlreadyExists() {
    given(businessFunctionService.findByIdOrThrowNotFound(businessFunction.getId())).willReturn(businessFunction);
    given(permissionService.findByIdOrThrowNotFound(permission.getId())).willReturn(permission);
    given(businessFunctionPermissionRepository.findByBusinessFunctionAndPermission(businessFunction,
        permission)).willReturn(Optional.of(businessFunctionPermission));

    var retrievedBusinessFunctionPermission = businessFunctionPermissionService.createOrRetrieveBusinessFunctionPermission(
        businessFunctionPermission);

    assertThat(retrievedBusinessFunctionPermission).isNotNull();
    assertThat(retrievedBusinessFunctionPermission.getId()).isEqualTo(businessFunctionPermission.getId());
  }

  @DisplayName("Handling when BusinessFunction is not found in findByExistingBusinessFunctionPermission")
  @Test
  void createOrRetrieveBusinessFunctionPermission_ThrowsResourceNotFoundException_WhenBusinessFunctionNotFoundInFind() {
    given(businessFunctionService.findByIdOrThrowNotFound(businessFunction.getId())).willThrow(
        ResourceNotFoundException.class);

    assertThrows(ResourceNotFoundException.class,
        () -> businessFunctionPermissionService.createOrRetrieveBusinessFunctionPermission(businessFunctionPermission));
  }

  @DisplayName("Handling when Permission is not found in findByExistingBusinessFunctionPermission")
  @Test
  void createOrRetrieveBusinessFunctionPermission_ThrowsResourceNotFoundException_WhenPermissionNotFoundInFind() {
    given(businessFunctionService.findByIdOrThrowNotFound(businessFunction.getId())).willReturn(
        businessFunction);
    given(permissionService.findByIdOrThrowNotFound(permission.getId())).willThrow(
        ResourceNotFoundException.class);

    assertThrows(ResourceNotFoundException.class,
        () -> businessFunctionPermissionService.createOrRetrieveBusinessFunctionPermission(businessFunctionPermission));
  }

  @DisplayName("Handling when BusinessFunctionPermission is not found")
  @Test
  void findByIdOrThrowNotFound_ThrowsResourceNotFoundException_WhenBusinessFunctionNotFound() {
    given(businessFunctionPermissionRepository.findById(businessFunctionPermission.getId())).willReturn(
        Optional.empty());

    assertThrows(ResourceNotFoundException.class,
        () -> businessFunctionPermissionService.findByIdOrThrowNotFound(businessFunctionPermission.getId()));
  }

  @Test
  @DisplayName("Returning a pageable list of business functions permissions by application name when successful")
  void listAll_ReturnListOfBusinessFunctionPermissionsByApplicationNameInsidePageObject_WhenSuccessful() {
    List<BusinessFunctionPermission> expectedBusinessFunctionPermissions = new ArrayList<>();
    expectedBusinessFunctionPermissions.add(businessFunctionPermission);

    var pageable = PageRequest.of(0, 5);
    var businessFunctionPermissionPage = new PageImpl<>(expectedBusinessFunctionPermissions, pageable,
        expectedBusinessFunctionPermissions.size());

    when(businessFunctionPermissionRepository.findByBusinessFunction_ApplicationName(
        businessFunction.getApplicationName(), pageable))
        .thenReturn(businessFunctionPermissionPage);

    var resultBusinessFunctionPage = businessFunctionPermissionService.listAllByApplicationName(
        businessFunction.getApplicationName(), pageable);

    verify(businessFunctionPermissionRepository).findByBusinessFunction_ApplicationName(
        businessFunction.getApplicationName(), pageable);

    assertThat(resultBusinessFunctionPage).isNotNull();
    assertThat(resultBusinessFunctionPage.toList()).isNotEmpty();
    assertThat(resultBusinessFunctionPage.toList().get(0).getBusinessFunction().getApplicationName())
        .isEqualTo(businessFunctionPermission.getBusinessFunction().getApplicationName());
  }
  @DisplayName("Deleting a business function permission successfully")
  @Test
  void deleteById_SuccessfullyDeleted_WhenNoAssociationsExist() {
    UUID businessFunctionPermissionId = businessFunctionPermission.getId();

    when(businessFunctionPermissionRepository.findById(businessFunctionPermissionId)).thenReturn(Optional.of(businessFunctionPermission));
    when(businessFunctionPermissionRepository.findBusinessFunctionPermissionWithProfileById(businessFunctionPermissionId)).thenReturn(Optional.empty());

    businessFunctionPermissionService.deleteById(businessFunctionPermissionId);

    verify(businessFunctionPermissionRepository).deleteById(businessFunctionPermissionId);
  }

  @DisplayName("Throwing CannotDeleteResourceException when associations exist")
  @Test
  void deleteById_ThrowsCannotDeleteResourceException_WhenAssociationsExist() {
    UUID businessFunctionPermissionId = businessFunctionPermission.getId();

    when(businessFunctionPermissionRepository.findById(businessFunctionPermissionId)).thenReturn(Optional.of(businessFunctionPermission));
    when(businessFunctionPermissionRepository.findBusinessFunctionPermissionWithProfileById(businessFunctionPermissionId)).thenReturn(Optional.of(businessFunctionPermission));

    assertThrows(CannotDeleteResourceException.class, () -> businessFunctionPermissionService.deleteById(businessFunctionPermissionId));
  }

  @DisplayName("Throwing ResourceNotFoundException when business function permission does not exist")
  @Test
  void deleteById_ThrowsResourceNotFoundException_WhenBusinessFunctionPermissionNotFound() {
    UUID businessFunctionPermissionId = businessFunctionPermission.getId();

    when(businessFunctionPermissionRepository.findById(businessFunctionPermissionId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> businessFunctionPermissionService.deleteById(businessFunctionPermissionId));
  }

  @DisplayName("Cascade delete business function permission successfully when ID exists")
  @Test
  void cascadeDeleteById_SuccessfullyDeleted_WhenIdExists() {
    UUID businessFunctionPermissionId = businessFunctionPermission.getId();

    when(businessFunctionPermissionRepository.findById(businessFunctionPermissionId)).thenReturn(Optional.of(businessFunctionPermission));

    businessFunctionPermissionService.cascadeDeleteById(businessFunctionPermissionId);

    verify(businessFunctionPermissionRepository).deleteProfileBusinessFunctionPermissionsByBusinessFunctionPermissionId(businessFunctionPermissionId);
    verify(businessFunctionPermissionRepository).deleteById(businessFunctionPermissionId);
  }

  @DisplayName("Throws ResourceNotFoundException when ID not found during cascade delete")
  @Test
  void cascadeDeleteById_ThrowsResourceNotFoundException_WhenIdNotFound() {
    UUID businessFunctionPermissionId = businessFunctionPermission.getId();

    when(businessFunctionPermissionRepository.findById(businessFunctionPermissionId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> businessFunctionPermissionService.cascadeDeleteById(businessFunctionPermissionId));
  }
}