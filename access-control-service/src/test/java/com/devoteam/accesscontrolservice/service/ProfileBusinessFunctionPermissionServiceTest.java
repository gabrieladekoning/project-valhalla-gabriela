package com.devoteam.accesscontrolservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.devoteam.accesscontrolservice.entity.BusinessFunction;
import com.devoteam.accesscontrolservice.entity.BusinessFunctionPermission;
import com.devoteam.accesscontrolservice.entity.Permission;
import com.devoteam.accesscontrolservice.entity.Profile;
import com.devoteam.accesscontrolservice.entity.ProfileBusinessFunctionPermission;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.repository.ProfileBusinessFunctionPermissionRepository;
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
class ProfileBusinessFunctionPermissionServiceTest {

  @Mock
  private BusinessFunctionPermissionService businessFunctionPermissionService;
  @Mock
  private ProfileService profileService;
  @Mock
  private ProfileBusinessFunctionPermissionRepository profileBusinessFunctionPermissionRepository;
  @InjectMocks
  private ProfileBusinessFunctionPermissionService profileBusinessFunctionPermissionService;
  private BusinessFunctionPermission businessFunctionPermission;
  private Profile profile;
  private ProfileBusinessFunctionPermission profileBusinessFunctionPermission;

  @BeforeEach
  public void setup() {
    var businessFunction = BusinessFunction.builder()
        .id(UUID.randomUUID())
        .applicationName("Application name")
        .functionName("Function name")
        .build();
    var permission = Permission.builder()
        .id(UUID.randomUUID())
        .name("Permission name")
        .build();
    businessFunctionPermission = BusinessFunctionPermission.builder()
        .id(UUID.randomUUID())
        .businessFunction(businessFunction)
        .permission(permission)
        .build();
    profile = Profile.builder()
        .id(UUID.randomUUID())
        .name("Profile name")
        .build();

    profileBusinessFunctionPermission = ProfileBusinessFunctionPermission.builder()
        .id(UUID.randomUUID())
        .businessFunctionPermission(businessFunctionPermission)
        .profile(profile)
        .build();
  }

  @DisplayName("Saving a new ProfileBusinessFunctionPermission successfully")
  @Test
  void createOrRetrieveProfileBusinessFunctionPermission_SuccessfullySaved_WhenAssociationDoesNotAlreadyExist() {
    given(businessFunctionPermissionService.findByIdOrThrowNotFound(
        businessFunctionPermission.getId())).willReturn(businessFunctionPermission);
    given(profileService.findByIdOrThrowNotFound(profile.getId())).willReturn(profile);
    given(profileBusinessFunctionPermissionRepository.findByBusinessFunctionPermissionAndProfile(
        businessFunctionPermission, profile)).willReturn(Optional.empty());
    given(profileBusinessFunctionPermissionRepository.save(ArgumentMatchers.any())).willReturn(
        profileBusinessFunctionPermission);

    var response = profileBusinessFunctionPermissionService.createOrRetrieveProfileBusinessFunctionPermission(
        profileBusinessFunctionPermission);

    assertThat(response).isNotNull();
    assertThat(response.getId()).isEqualTo(profileBusinessFunctionPermission.getId());
  }

  @DisplayName("Retrieving an existing ProfileBusinessFunctionPermission successfully")
  @Test
  void createOrRetrieveProfileBusinessFunctionPermission_SuccessfullyRetrieved_WhenAssociationAlreadyExists() {
    given(businessFunctionPermissionService.findByIdOrThrowNotFound(
        businessFunctionPermission.getId())).willReturn(businessFunctionPermission);
    given(profileService.findByIdOrThrowNotFound(profile.getId())).willReturn(profile);
    given(profileBusinessFunctionPermissionRepository.findByBusinessFunctionPermissionAndProfile(
        businessFunctionPermission, profile)).willReturn(Optional.of(profileBusinessFunctionPermission));

    var response = profileBusinessFunctionPermissionService.createOrRetrieveProfileBusinessFunctionPermission(
        profileBusinessFunctionPermission);

    assertThat(response).isNotNull();
    assertThat(response.getId()).isEqualTo(profileBusinessFunctionPermission.getId());
  }

  @DisplayName("Handling when BusinessFunctionPermission is not found")
  @Test
  void createOrRetrieveProfileBusinessFunctionPermission_ThrowsResourceNotFoundException_WhenBusinessFunctionPermissionNotFound() {
    given(businessFunctionPermissionService.findByIdOrThrowNotFound(
        businessFunctionPermission.getId())).willThrow(ResourceNotFoundException.class);

    assertThrows(ResourceNotFoundException.class,
        () -> profileBusinessFunctionPermissionService.createOrRetrieveProfileBusinessFunctionPermission(
            profileBusinessFunctionPermission));
  }

  @DisplayName("Handling when Profile is not found")
  @Test
  void createOrRetrieveProfileBusinessFunctionPermission_ThrowsResourceNotFoundException_WhenProfileNotFound() {
    given(businessFunctionPermissionService.findByIdOrThrowNotFound(
        businessFunctionPermission.getId())).willReturn(businessFunctionPermission);
    given(profileService.findByIdOrThrowNotFound(profile.getId())).willThrow(
        ResourceNotFoundException.class);

    assertThrows(ResourceNotFoundException.class,
        () -> profileBusinessFunctionPermissionService.createOrRetrieveProfileBusinessFunctionPermission(
            profileBusinessFunctionPermission));
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