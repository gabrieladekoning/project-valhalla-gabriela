package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.ProfileCreationRequest;
import com.devoteam.accesscontrolservice.dto.ProfileCreationResponse;
import com.devoteam.accesscontrolservice.entity.Profile;
import com.devoteam.accesscontrolservice.mapper.ProfileMapper;
import com.devoteam.accesscontrolservice.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {
    private final ProfileRepository profileRepository;

    public ProfileCreationResponse createOrRetrieveProfile(ProfileCreationRequest creationRequest) {
        log.info("Received a request to create a new profile with name: {}", creationRequest.getName());

        var existingProfile = findExistingProfile(creationRequest);

        return existingProfile
                .map(ProfileMapper.INSTANCE::mapToProfileResponse)
                .orElseGet(() -> createNewProfile(creationRequest));
    }

    private Optional<Profile> findExistingProfile(ProfileCreationRequest creationRequest) {
        var existingProfile = profileRepository.findByName(creationRequest.getName());
        existingProfile.ifPresent(profile ->
                log.info("A profile with name: {} already exists, returning the existing profile",
                        creationRequest.getName())
        );
        return existingProfile;
    }

    private ProfileCreationResponse createNewProfile(ProfileCreationRequest creationRequest) {
        var profile = ProfileMapper.INSTANCE.mapToProfile(creationRequest);
        var savedProfile = profileRepository.save(profile);

        log.debug("Created a new profile with ID: {}", savedProfile.getId());
        return ProfileMapper.INSTANCE.mapToProfileResponse(savedProfile);
    }
}