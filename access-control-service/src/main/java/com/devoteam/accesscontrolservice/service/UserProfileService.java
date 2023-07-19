package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.UserProfileCreationRequest;
import com.devoteam.accesscontrolservice.dto.UserProfileCreationResponse;
import com.devoteam.accesscontrolservice.entity.UserProfile;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.mapper.UserProfileMapper;
import com.devoteam.accesscontrolservice.repository.ProfileRepository;
import com.devoteam.accesscontrolservice.repository.UserProfileRepository;
import com.devoteam.accesscontrolservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final UserProfileRepository userProfileRepository;

    public UserProfileCreationResponse createOrRetrieveUserProfile(UserProfileCreationRequest creationRequest) {
        log.debug("Received a request to create an association between User ID: {} and Profile ID: {}",
                creationRequest.getApplicationUserId(), creationRequest.getProfileId());

        var existingAssociation = findByExistingUserProfile(creationRequest);

        return existingAssociation
                .map(UserProfileMapper.INSTANCE::mapToUserProfileResponse)
                .orElseGet(() -> createNewUserProfile(creationRequest));
    }

    private Optional<UserProfile> findByExistingUserProfile(UserProfileCreationRequest creationRequest) {
        var userId = UUID.fromString(creationRequest.getApplicationUserId());
        var profileId = creationRequest.getProfileId();

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with ID: %s not found.".formatted(userId)
                ));

        var profile = profileRepository.findById(UUID.fromString(profileId))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Profile with ID: %s not found.".formatted(profileId)
                ));
        return userProfileRepository.findByApplicationUserAndProfile(user, profile);
    }

    private UserProfileCreationResponse createNewUserProfile(UserProfileCreationRequest creationRequest) {
        var userProfile = UserProfileMapper.INSTANCE.mapToUserProfile(creationRequest);
        var savedUserProfile = userProfileRepository.save(userProfile);

        log.debug("Created a new profile with ID: {}", savedUserProfile.getId());
        return UserProfileMapper.INSTANCE.mapToUserProfileResponse(savedUserProfile);
    }
}