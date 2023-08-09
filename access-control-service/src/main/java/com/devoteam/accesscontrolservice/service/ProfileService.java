package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.ProfileCreationRequest;
import com.devoteam.accesscontrolservice.entity.Profile;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.mapper.ProfileMapper;
import com.devoteam.accesscontrolservice.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {
    private final ProfileRepository profileRepository;

    public Profile createOrRetrieveProfile(ProfileCreationRequest creationRequest) {
        log.info("Received a request to create a new profile with name: '{}'", creationRequest.getName());

        return findExistingProfile(creationRequest)
                .orElseGet(() -> createNewProfile(creationRequest));
    }

    private Optional<Profile> findExistingProfile(ProfileCreationRequest creationRequest) {
        log.info("Checking if profile exists '{}'", creationRequest);

        return profileRepository.findByName(creationRequest.getName());
    }

    private Profile createNewProfile(ProfileCreationRequest creationRequest) {
        log.info("Creating new profile '{}'", creationRequest);

        var profile = ProfileMapper.INSTANCE.mapToProfile(creationRequest);

        return profileRepository.save(profile);
    }

    public Profile findByIdOrThrowNotFound(String profileId) {
        log.info("Finding profile by ID: '{}'", profileId);

        return profileRepository.findById(UUID.fromString(profileId))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Profile with ID: %s not found.".formatted(profileId)
                ));
    }
}