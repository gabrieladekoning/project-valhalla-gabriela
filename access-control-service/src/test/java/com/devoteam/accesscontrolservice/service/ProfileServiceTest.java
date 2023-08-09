package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.ProfileCreationRequest;
import com.devoteam.accesscontrolservice.entity.Profile;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {
    @Mock
    ProfileRepository profileRepository;
    @InjectMocks
    ProfileService profileService;
    private ProfileCreationRequest creationRequest;
    private Profile profile;

    @BeforeEach
    public void setup() {
        UUID uuid = UUID.randomUUID();
        creationRequest = ProfileCreationRequest.builder()
                .name("Profile name")
                .build();
        profile = Profile.builder()
                .id(uuid)
                .name(creationRequest.getName())
                .build();
    }

    @DisplayName("Saving a new profile successfully")
    @Test
    void createOrRetrieveProfile_SuccessfullySaved_WhenFunctionDoesNotAlreadyExist() {
        given(profileRepository.findByName(creationRequest.getName())).willReturn(Optional.empty());
        given(profileRepository.save(ArgumentMatchers.any())).willReturn(profile);

        var returnedProfile = profileService.createOrRetrieveProfile(creationRequest);

        Assertions.assertThat(returnedProfile).hasNoNullFieldsOrProperties();
        Assertions.assertThat(returnedProfile.getId()).isEqualTo(profile.getId());
    }

    @DisplayName("Retrieving an existing profile successfully")
    @Test
    void createOrRetrieveProfile_SuccessfullyRetrieved_WhenFunctionAlreadyExists() {
        given(profileRepository.findByName(creationRequest.getName())).willReturn(Optional.of(profile));

        var returnedProfile = profileService.createOrRetrieveProfile(creationRequest);

        Assertions.assertThat(returnedProfile).isNotNull();
        Assertions.assertThat(returnedProfile.getId()).isEqualTo(profile.getId());
    }

    @DisplayName("Handling when profile is not found")
    @Test
    void findByIdOrThrowNotFound_ThrowsResourceNotFoundException_WhenProfileNotFound() {
        given(profileRepository.findById(profile.getId())).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> profileService.findByIdOrThrowNotFound(String.valueOf(profile.getId())));
    }
}