package com.devoteam.accesscontrolservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.devoteam.accesscontrolservice.entity.Profile;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.repository.ProfileRepository;
import java.util.ArrayList;
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
class ProfileServiceTest {

  @Mock
  ProfileRepository profileRepository;
  @InjectMocks
  ProfileService profileService;
  private Profile profile;
  private Profile secondProfile;

  @BeforeEach
  public void setup() {
    profile = Profile.builder()
        .id(UUID.randomUUID())
        .name("Admin")
        .build();
    secondProfile = Profile.builder()
        .id(UUID.randomUUID())
        .name("User")
        .build();
  }

  @DisplayName("Saving a new profile successfully")
  @Test
  void createOrRetrieveProfile_SuccessfullySaved_WhenFunctionDoesNotAlreadyExist() {
    given(profileRepository.findByName(profile.getName())).willReturn(Optional.empty());
    given(profileRepository.save(ArgumentMatchers.any())).willReturn(profile);

    var returnedProfile = profileService.createOrRetrieveProfile(profile);

    Assertions.assertThat(returnedProfile.getId()).isNotNull();
    Assertions.assertThat(returnedProfile.getName()).isNotNull();
    Assertions.assertThat(returnedProfile.getId()).isEqualTo(profile.getId());
  }

  @DisplayName("Retrieving an existing profile successfully")
  @Test
  void createOrRetrieveProfile_SuccessfullyRetrieved_WhenFunctionAlreadyExists() {
    given(profileRepository.findByName(profile.getName())).willReturn(Optional.of(profile));

    var returnedProfile = profileService.createOrRetrieveProfile(profile);

    Assertions.assertThat(returnedProfile).isNotNull();
    Assertions.assertThat(returnedProfile.getId()).isEqualTo(profile.getId());
  }

  @DisplayName("Handling when profile is not found")
  @Test
  void findByIdOrThrowNotFound_ThrowsResourceNotFoundException_WhenProfileNotFound() {
    given(profileRepository.findById(profile.getId())).willReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class,
        () -> profileService.findByIdOrThrowNotFound(profile.getId()));
  }

  @Test
  @DisplayName("Returning a pageable list of profiles when successful")
  void listAll_ReturnListOfProfilesInsidePageObject_WhenSuccessful() {
    List<Profile> expectedProfiles = new ArrayList<>();
    expectedProfiles.add(profile);
    expectedProfiles.add(secondProfile);

    var pageable = PageRequest.of(0, 5);
    var profilePage = new PageImpl<>(expectedProfiles, pageable, expectedProfiles.size());

    when(profileRepository.findAll(pageable)).thenReturn(profilePage);

    var resultProfilePage = profileService.listAll(pageable);

    verify(profileRepository).findAll(pageable);

    assertThat(resultProfilePage).isNotNull();
    assertThat(resultProfilePage.toList()).isNotEmpty();
    assertThat(resultProfilePage.toList().get(0).getName()).isEqualTo(profile.getName());
    assertThat(resultProfilePage.toList().get(1).getName()).isEqualTo(secondProfile.getName());
  }

  @DisplayName("Updating a profile successfully")
  @Test
  void update_SuccessfullyUpdated_WhenProfileExists() {
    var updatedProfile = Profile.builder()
        .id(profile.getId())
        .name("Delete")
        .build();

    given(profileRepository.findById(profile.getId())).willReturn(Optional.of(profile));

    profileService.update(updatedProfile);

    verify(profileRepository).findById(profile.getId());
    verify(profileRepository).save(any(Profile.class));
  }

  @DisplayName("Handling when profile is not found during update")
  @Test
  void update_ThrowsResourceNotFoundException_WhenProfileNotFound() {
    var nonExistentProfile = Profile.builder()
        .id(UUID.randomUUID())
        .name("Does not exist")
        .build();

    given(profileRepository.findById(nonExistentProfile.getId())).willReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> profileService.update(nonExistentProfile));
  }
}