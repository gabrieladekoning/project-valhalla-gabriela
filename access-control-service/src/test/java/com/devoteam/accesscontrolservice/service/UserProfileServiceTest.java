package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.UserProfileCreationRequest;
import com.devoteam.accesscontrolservice.dto.UserProfileCreationResponse;
import com.devoteam.accesscontrolservice.entity.ApplicationUser;
import com.devoteam.accesscontrolservice.entity.UserProfile;
import com.devoteam.accesscontrolservice.entity.Profile;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.repository.UserProfileRepository;
import com.devoteam.accesscontrolservice.repository.UserRepository;
import com.devoteam.accesscontrolservice.repository.ProfileRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private UserProfileService userProfileService;

    private UserProfileCreationRequest creationRequest;
    private ApplicationUser applicationUser;
    private Profile profile;
    private UserProfile userProfile;

    @BeforeEach
    public void setup() {
        UUID userId = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();

        creationRequest = UserProfileCreationRequest.builder()
                .applicationUserId(String.valueOf(userId))
                .profileId(String.valueOf(profileId))
                .build();

        applicationUser = ApplicationUser.builder()
                .applicationUserId(userId)
                .firstName("First name")
                .lastName("Last name")
                .email("thisisan@email.com")
                .build();

        profile = Profile.builder()
                .profileId(profileId)
                .name("Profile name")
                .build();

        userProfile = UserProfile.builder()
                .userProfileId(UUID.randomUUID())
                .applicationUser(applicationUser)
                .profile(profile)
                .build();
    }

    @DisplayName("Test saving a new UserProfile successfully")
    @Test
    void createOrRetrieveUserProfile_SuccessfullySaved_WhenAssociationDoesNotAlreadyExist() {
        given(userRepository.findById(applicationUser.getApplicationUserId())).willReturn(Optional.of(applicationUser));
        given(profileRepository.findById(profile.getProfileId())).willReturn(Optional.of(profile));
        given(userProfileRepository.findByApplicationUserAndProfile(applicationUser, profile)).willReturn(Optional.empty());
        given(userProfileRepository.save(ArgumentMatchers.any())).willReturn(userProfile);

        UserProfileCreationResponse savedUserProfile = userProfileService.createOrRetrieveUserProfile(creationRequest);

        Assertions.assertThat(savedUserProfile).hasNoNullFieldsOrProperties();
        Assertions.assertThat(savedUserProfile.getUserProfileId()).isEqualTo(userProfile.getUserProfileId().toString());
    }

    @DisplayName("Test retrieving an existing UserProfile successfully")
    @Test
    void createOrRetrieveUserProfile_SuccessfullyRetrieved_WhenAssociationAlreadyExists() {
        given(userRepository.findById(applicationUser.getApplicationUserId())).willReturn(Optional.of(applicationUser));
        given(profileRepository.findById(profile.getProfileId())).willReturn(Optional.of(profile));
        given(userProfileRepository.findByApplicationUserAndProfile(applicationUser, profile)).willReturn(Optional.of(userProfile));

        UserProfileCreationResponse savedUserProfile = userProfileService.createOrRetrieveUserProfile(creationRequest);

        Assertions.assertThat(savedUserProfile).isNotNull();
        Assertions.assertThat(savedUserProfile.getUserProfileId()).isEqualTo(userProfile.getUserProfileId().toString());
    }

    @DisplayName("Test handling when User is not found")
    @Test
    void createOrRetrieveUserProfile_ThrowsResourceNotFoundException_WhenUserNotFound() {
        given(userRepository.findById(applicationUser.getApplicationUserId())).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userProfileService.createOrRetrieveUserProfile(creationRequest));
    }

    @DisplayName("Test handling when Profile is not found")
    @Test
    void createOrRetrieveUserProfile_ThrowsResourceNotFoundException_WhenProfileNotFound() {
        given(userRepository.findById(applicationUser.getApplicationUserId())).willReturn(Optional.of(applicationUser));
        given(profileRepository.findById(profile.getProfileId())).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userProfileService.createOrRetrieveUserProfile(creationRequest));
    }
}