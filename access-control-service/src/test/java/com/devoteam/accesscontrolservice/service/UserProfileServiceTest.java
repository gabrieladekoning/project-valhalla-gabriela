package com.devoteam.accesscontrolservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.devoteam.accesscontrolservice.dto.ApplicationUserReadResponse;
import com.devoteam.accesscontrolservice.dto.ProfileWithUsersReadResponse;
import com.devoteam.accesscontrolservice.dto.UserProfileCreationRequest;
import com.devoteam.accesscontrolservice.entity.ApplicationUser;
import com.devoteam.accesscontrolservice.entity.Profile;
import com.devoteam.accesscontrolservice.entity.UserProfile;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.mapper.ProfileMapper;
import com.devoteam.accesscontrolservice.mapper.UserProfileMapper;
import com.devoteam.accesscontrolservice.repository.UserProfileRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

  @Mock
  private UserService userService;

  @Mock
  private ProfileService profileService;
  @Mock
  private UserProfileRepository userProfileRepository;
  @Mock
  private UserProfileMapper userProfileMapper;
  @Mock
  private ProfileMapper profileMapper;
  @InjectMocks
  private UserProfileService userProfileService;

  private UserProfileCreationRequest creationRequest;
  private ApplicationUser applicationUser;
  private Profile profile;
  private UserProfile userProfile;
  private ApplicationUserReadResponse applicationUserReadResponse;
  private ProfileWithUsersReadResponse profileWithUsersReadResponse;


  @BeforeEach
  public void setup() {
    UUID userId = UUID.randomUUID();
    UUID profileId = UUID.randomUUID();

    creationRequest = UserProfileCreationRequest.builder()
        .applicationUserId(String.valueOf(userId))
        .profileId(String.valueOf(profileId))
        .build();

    applicationUser = ApplicationUser.builder()
        .id(userId)
        .firstName("First name")
        .lastName("Last name")
        .email("thisisan@email.com")
        .build();

    profile = Profile.builder()
        .id(profileId)
        .name("Profile name")
        .build();

    userProfile = UserProfile.builder()
        .id(UUID.randomUUID())
        .applicationUser(applicationUser)
        .profile(profile)
        .build();

    applicationUserReadResponse = ApplicationUserReadResponse.builder()
        .id(userId.toString())
        .email(applicationUser.getEmail())
        .build();

    profileWithUsersReadResponse = ProfileWithUsersReadResponse.builder()
        .id(profileId.toString())
        .name(profile.getName())
        .users(List.of(applicationUserReadResponse))
        .build();
  }

  @DisplayName("Saving a new UserProfile successfully")
  @Test
  void createOrRetrieveUserProfile_SuccessfullySaved_WhenAssociationDoesNotAlreadyExist() {
    given(userService.findByIdOrThrowNotFound(String.valueOf(applicationUser.getId()))).willReturn(applicationUser);
    given(profileService.findByIdOrThrowNotFound(String.valueOf(profile.getId()))).willReturn(profile);
    given(userProfileRepository.findByApplicationUserAndProfile(applicationUser, profile)).willReturn(Optional.empty());
    given(userProfileRepository.save(ArgumentMatchers.any())).willReturn(userProfile);

    var savedUserProfile = userProfileService.createOrRetrieveUserProfile(creationRequest);

    Assertions.assertThat(savedUserProfile).hasNoNullFieldsOrProperties();
    Assertions.assertThat(savedUserProfile.getId()).isEqualTo(userProfile.getId());
  }

  @DisplayName("Retrieving an existing UserProfile successfully")
  @Test
  void createOrRetrieveUserProfile_SuccessfullyRetrieved_WhenAssociationAlreadyExists() {
    given(userService.findByIdOrThrowNotFound(String.valueOf(applicationUser.getId()))).willReturn(applicationUser);
    given(profileService.findByIdOrThrowNotFound(String.valueOf(profile.getId()))).willReturn(profile);
    given(userProfileRepository.findByApplicationUserAndProfile(applicationUser, profile)).willReturn(
        Optional.of(userProfile));

    var savedUserProfile = userProfileService.createOrRetrieveUserProfile(creationRequest);

    Assertions.assertThat(savedUserProfile).isNotNull();
    Assertions.assertThat(savedUserProfile.getId()).isEqualTo(userProfile.getId());
  }

  @DisplayName("Handling when User is not found")
  @Test
  void createOrRetrieveUserProfile_ThrowsResourceNotFoundException_WhenUserNotFound() {
    given(userService.findByIdOrThrowNotFound(String.valueOf(applicationUser.getId()))).willThrow(
        new ResourceNotFoundException("User not found"));

    assertThrows(ResourceNotFoundException.class,
        () -> userProfileService.createOrRetrieveUserProfile(creationRequest));
  }

  @DisplayName("Handling when Profile is not found")
  @Test
  void createOrRetrieveUserProfile_ThrowsResourceNotFoundException_WhenProfileNotFound() {
    given(userService.findByIdOrThrowNotFound(String.valueOf(applicationUser.getId()))).willReturn(applicationUser);
    given(profileService.findByIdOrThrowNotFound(String.valueOf(profile.getId()))).willThrow(
        new ResourceNotFoundException("Profile not found"));

    assertThrows(ResourceNotFoundException.class,
        () -> userProfileService.createOrRetrieveUserProfile(creationRequest));
  }

  @DisplayName("Returning a pageable list of profiles with associated users when successful")
  @Test
  void getAllProfilesWithAssociatedUsers_ReturnsPageOfProfilesWithUsers_WhenSuccessful() {
    List<UserProfile> userProfiles = List.of(userProfile);
    PageRequest pageable = PageRequest.of(0, 5);
    Page<UserProfile> userProfilesPage = new PageImpl<>(userProfiles, pageable, userProfiles.size());

    when(userProfileRepository.findAllWithApplicationUserAndProfile(pageable)).thenReturn(userProfilesPage);
    when(userProfileMapper.mapToUserReadResponse(userProfile)).thenReturn(applicationUserReadResponse);
    when(profileMapper.mapToProfileWithUsersReadResponse(profile, List.of(applicationUserReadResponse))).thenReturn(
        profileWithUsersReadResponse);

    Page<ProfileWithUsersReadResponse> result = userProfileService.getAllProfilesWithAssociatedUsers(pageable);

    assertThat(result).isNotNull();
    assertThat(result.getContent()).isNotEmpty();
    assertThat(result.getContent().get(0).getId()).isEqualTo(profileWithUsersReadResponse.getId());
    assertThat(result.getContent().get(0).getName()).isEqualTo(profileWithUsersReadResponse.getName());
    assertThat(result.getContent().get(0).getUsers()).isEqualTo(profileWithUsersReadResponse.getUsers());
  }
}