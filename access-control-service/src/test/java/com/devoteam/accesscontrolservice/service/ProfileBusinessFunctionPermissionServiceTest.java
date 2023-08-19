package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.ProfileBusinessFunctionPermissionCreationRequest;
import com.devoteam.accesscontrolservice.entity.*;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.mapper.ProfileBusinessFunctionPermissionMapper;
import com.devoteam.accesscontrolservice.repository.ProfileBusinessFunctionPermissionRepository;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileBusinessFunctionPermissionServiceTest {

  @Mock
  private BusinessFunctionPermissionService businessFunctionPermissionService;

  @Mock
  private ProfileService profileService;

  @Mock
  private ProfileBusinessFunctionPermissionRepository profileBusinessFunctionPermissionRepository;
  @Mock
  private ProfileBusinessFunctionPermissionMapper profileBusinessFunctionPermissionMapper;

  @InjectMocks
  private ProfileBusinessFunctionPermissionService profileBusinessFunctionPermissionService;

  private ProfileBusinessFunctionPermissionCreationRequest creationRequest;
  private BusinessFunctionPermission businessFunctionPermission;
  private Profile profile;
  private ProfileBusinessFunctionPermission profileBusinessFunctionPermission;

  @BeforeEach
  public void setup() {
    UUID profileBusinessFunctionPermissionId = UUID.randomUUID();
    UUID businessFunctionPermissionId = UUID.randomUUID();
    UUID businessFunctionId = UUID.randomUUID();
    UUID permissionId = UUID.randomUUID();
    UUID profileId = UUID.randomUUID();

    creationRequest = ProfileBusinessFunctionPermissionCreationRequest.builder()
        .businessFunctionPermissionId(String.valueOf(businessFunctionPermissionId))
        .profileId(String.valueOf(profileId))
        .build();

    BusinessFunction businessFunction = BusinessFunction.builder()
        .id(businessFunctionId)
        .applicationName("Application name")
        .functionName("Function name")
        .build();

    Permission permission = Permission.builder()
        .id(permissionId)
        .name("Permission name")
        .build();

    businessFunctionPermission = BusinessFunctionPermission.builder()
        .id(businessFunctionPermissionId)
        .businessFunction(businessFunction)
        .permission(permission)
        .build();

    profile = Profile.builder()
        .id(profileId)
        .name("Profile name")
        .build();

    profileBusinessFunctionPermission = ProfileBusinessFunctionPermission.builder()
        .id(profileBusinessFunctionPermissionId)
        .businessFunctionPermission(businessFunctionPermission)
        .profile(profile)
        .build();
  }

  @DisplayName("Saving a new ProfileBusinessFunctionPermission successfully")
  @Test
  void createOrRetrieveProfileBusinessFunctionPermission_SuccessfullySaved_WhenAssociationDoesNotAlreadyExist() {
    given(businessFunctionPermissionService.findByIdOrThrowNotFound(
        creationRequest.getBusinessFunctionPermissionId())).willReturn(businessFunctionPermission);
    given(profileService.findByIdOrThrowNotFound(creationRequest.getProfileId())).willReturn(profile);
    given(profileBusinessFunctionPermissionRepository.findByBusinessFunctionPermissionAndProfile(
        businessFunctionPermission, profile)).willReturn(Optional.empty());
    given(profileBusinessFunctionPermissionRepository.save(ArgumentMatchers.any())).willReturn(
        profileBusinessFunctionPermission);

    var response = profileBusinessFunctionPermissionService.createOrRetrieveProfileBusinessFunctionPermission(
        creationRequest);

    assertThat(response).isNotNull();
    assertThat(response.getId()).isEqualTo(profileBusinessFunctionPermission.getId());
  }

  @DisplayName("Retrieving an existing ProfileBusinessFunctionPermission successfully")
  @Test
  void createOrRetrieveProfileBusinessFunctionPermission_SuccessfullyRetrieved_WhenAssociationAlreadyExists() {
    given(businessFunctionPermissionService.findByIdOrThrowNotFound(
        creationRequest.getBusinessFunctionPermissionId())).willReturn(businessFunctionPermission);
    given(profileService.findByIdOrThrowNotFound(creationRequest.getProfileId())).willReturn(profile);
    given(profileBusinessFunctionPermissionRepository.findByBusinessFunctionPermissionAndProfile(
        businessFunctionPermission, profile)).willReturn(Optional.of(profileBusinessFunctionPermission));

    var response = profileBusinessFunctionPermissionService.createOrRetrieveProfileBusinessFunctionPermission(
        creationRequest);

    assertThat(response).isNotNull();
    assertThat(response.getId()).isEqualTo(profileBusinessFunctionPermission.getId());
  }

  @DisplayName("Handling when BusinessFunctionPermission is not found")
  @Test
  void createOrRetrieveProfileBusinessFunctionPermission_ThrowsResourceNotFoundException_WhenBusinessFunctionPermissionNotFound() {
    given(businessFunctionPermissionService.findByIdOrThrowNotFound(
        creationRequest.getBusinessFunctionPermissionId())).willThrow(ResourceNotFoundException.class);

    assertThrows(ResourceNotFoundException.class,
        () -> profileBusinessFunctionPermissionService.createOrRetrieveProfileBusinessFunctionPermission(
            creationRequest));
  }

  @DisplayName("Handling when Profile is not found")
  @Test
  void createOrRetrieveProfileBusinessFunctionPermission_ThrowsResourceNotFoundException_WhenProfileNotFound() {
    given(businessFunctionPermissionService.findByIdOrThrowNotFound(
        creationRequest.getBusinessFunctionPermissionId())).willReturn(businessFunctionPermission);
    given(profileService.findByIdOrThrowNotFound(creationRequest.getProfileId())).willThrow(
        ResourceNotFoundException.class);

    assertThrows(ResourceNotFoundException.class,
        () -> profileBusinessFunctionPermissionService.createOrRetrieveProfileBusinessFunctionPermission(
            creationRequest));
  }

  @DisplayName("Returning a pageable list of profile business functions permissions by profile name when successful")
  @Test
  void listAll_ReturnListOfProfileBusinessFunctionPermissionsByProfileNameInsidePageObject_WhenSuccessful() {
    List<ProfileBusinessFunctionPermission> expectedProfileBusinessFunctionPermissions = new ArrayList<>();
    expectedProfileBusinessFunctionPermissions.add(profileBusinessFunctionPermission);

    var pageable = PageRequest.of(0, 5);
    var profileBusinessFunctionPermissionPage = new PageImpl<>(expectedProfileBusinessFunctionPermissions, pageable,
        expectedProfileBusinessFunctionPermissions.size());

    when(profileBusinessFunctionPermissionRepository.findByProfile_Name(
        profileBusinessFunctionPermission.getProfile().getName(), pageable))
        .thenReturn(profileBusinessFunctionPermissionPage);

    var resultProfileBusinessFunctionPage = profileBusinessFunctionPermissionService.listAllByProfileName(
        profileBusinessFunctionPermission.getProfile().getName(), pageable);

    verify(profileBusinessFunctionPermissionRepository).findByProfile_Name(
        profileBusinessFunctionPermission.getProfile().getName(), pageable);

    assertThat(resultProfileBusinessFunctionPage).isNotNull();
    assertThat(resultProfileBusinessFunctionPage.toList()).isNotEmpty();
    assertThat(resultProfileBusinessFunctionPage.toList().get(0).getId())
        .isEqualTo(profileBusinessFunctionPermission.getId());
    assertThat(resultProfileBusinessFunctionPage.toList().get(0).getProfile().getName())
        .isEqualTo(profileBusinessFunctionPermission.getProfile().getName());
    assertThat(resultProfileBusinessFunctionPage.toList().get(0).getBusinessFunctionPermission())
        .isEqualTo(profileBusinessFunctionPermission.getBusinessFunctionPermission());
  }
}