package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.UserProfileCreationRequest;
import com.devoteam.accesscontrolservice.entity.UserProfile;
import com.devoteam.accesscontrolservice.mapper.UserProfileMapper;
import com.devoteam.accesscontrolservice.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService {
    private final UserService userService;
    private final ProfileService profileService;
    private final UserProfileRepository userProfileRepository;

    public UserProfile createOrRetrieveUserProfile(UserProfileCreationRequest creationRequest) {
        log.debug("Received a request to create an association between user ID: {} and profile ID: {}",
                creationRequest.getApplicationUserId(), creationRequest.getProfileId());

        return findByExistingUserProfile(creationRequest)
                .orElseGet(() -> createNewUserProfile(creationRequest));
    }

    private Optional<UserProfile> findByExistingUserProfile(UserProfileCreationRequest creationRequest) {
        var user = userService.findByIdOrThrowNotFound(creationRequest.getApplicationUserId());
        var profile = profileService.findByIdOrThrowNotFound(creationRequest.getProfileId());
        return userProfileRepository.findByApplicationUserAndProfile(user, profile);
    }

    private UserProfile createNewUserProfile(UserProfileCreationRequest creationRequest) {
        var userProfile = UserProfileMapper.INSTANCE.mapToUserProfile(creationRequest);
        return userProfileRepository.save(userProfile);
    }
}