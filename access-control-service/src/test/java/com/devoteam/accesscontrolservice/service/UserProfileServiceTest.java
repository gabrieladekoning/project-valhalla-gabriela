package com.devoteam.accesscontrolservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.devoteam.accesscontrolservice.entity.ApplicationUser;
import com.devoteam.accesscontrolservice.entity.Profile;
import com.devoteam.accesscontrolservice.entity.UserProfile;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
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
  @InjectMocks
  private UserProfileService userProfileService;
  private ApplicationUser applicationUser;
  private Profile profile;
  private UserProfile userProfile;

  @BeforeEach
  public void setup() {
    applicationUser = ApplicationUser.builder()
        .id(UUID.randomUUID())
        .firstName("First name")
        .lastName("Last name")
        .email("thisisan@email.com")
        .build();
    profile = Profile.builder()
        .id(UUID.randomUUID())
        .name("Profile name")
        .build();

    userProfile = UserProfile.builder()
        .id(UUID.randomUUID())
        .applicationUser(applicationUser)
        .profile(profile)
        .build();
  }

  @DisplayName("Saving a new UserProfile successfully")
  @Test
  void createOrRetrieveUserProfile_SuccessfullySaved_WhenAssociationDoesNotAlreadyExist() {
    given(userService.findByIdOrThrowNotFound(applicationUser.getId())).willReturn(applicationUser);
    given(profileService.findByIdOrThrowNotFound(profile.getId())).willReturn(profile);
    given(userProfileRepository.findByApplicationUserAndProfile(applicationUser, profile)).willReturn(Optional.empty());
    given(userProfileRepository.save(ArgumentMatchers.any())).willReturn(userProfile);

    var savedUserProfile = userProfileService.createOrRetrieveUserProfile(userProfile);

    Assertions.assertThat(savedUserProfile).hasNoNullFieldsOrProperties();
    Assertions.assertThat(savedUserProfile.getId()).isEqualTo(userProfile.getId());
  }

  @DisplayName("Retrieving an existing UserProfile successfully")
  @Test
  void createOrRetrieveUserProfile_SuccessfullyRetrieved_WhenAssociationAlreadyExists() {
    given(userService.findByIdOrThrowNotFound(applicationUser.getId())).willReturn(applicationUser);
    given(profileService.findByIdOrThrowNotFound(profile.getId())).willReturn(profile);
    given(userProfileRepository.findByApplicationUserAndProfile(applicationUser, profile)).willReturn(
        Optional.of(userProfile));

    var savedUserProfile = userProfileService.createOrRetrieveUserProfile(userProfile);

    Assertions.assertThat(savedUserProfile).isNotNull();
    Assertions.assertThat(savedUserProfile.getId()).isEqualTo(userProfile.getId());
  }

  @DisplayName("Handling when User is not found")
  @Test
  void createOrRetrieveUserProfile_ThrowsResourceNotFoundException_WhenUserNotFound() {
    given(userService.findByIdOrThrowNotFound(applicationUser.getId())).willThrow(
        new ResourceNotFoundException("User not found"));

    assertThrows(ResourceNotFoundException.class,
        () -> userProfileService.createOrRetrieveUserProfile(userProfile));
  }

  @DisplayName("Handling when Profile is not found")
  @Test
  void createOrRetrieveUserProfile_ThrowsResourceNotFoundException_WhenProfileNotFound() {
    given(userService.findByIdOrThrowNotFound(applicationUser.getId())).willReturn(applicationUser);
    given(profileService.findByIdOrThrowNotFound(profile.getId())).willThrow(
        new ResourceNotFoundException("Profile not found"));

    assertThrows(ResourceNotFoundException.class,
        () -> userProfileService.createOrRetrieveUserProfile(userProfile));
  }

  @DisplayName("Returning a pageable list of profiles with associated users when successful")
  @Test
  void getAllProfilesWithAssociatedUsers_ReturnsPageOfProfilesWithUsers_WhenSuccessful() {
    var pageable = PageRequest.of(0, 5);

    var expectedPage = new PageImpl<>(List.of(userProfile), pageable, 1);
    when(userProfileRepository.findAllWithApplicationUserAndProfile(pageable)).thenReturn(expectedPage);

    var resultPage = userProfileService.getAllProfilesWithAssociatedUsers(pageable);

    assertEquals(expectedPage, resultPage);
    verify(userProfileRepository, times(1)).findAllWithApplicationUserAndProfile(pageable);
  }
}