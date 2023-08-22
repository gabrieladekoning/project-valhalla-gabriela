package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.entity.UserProfile;
import com.devoteam.accesscontrolservice.repository.UserProfileRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService {

  private final UserService userService;
  private final ProfileService profileService;
  private final UserProfileRepository userProfileRepository;


  public UserProfile createOrRetrieveUserProfile(UserProfile userProfile) {
    log.info("Received a request to create an association between user ID: '{}' and profile ID: '{}'",
        userProfile.getApplicationUser().getId(), userProfile.getProfile().getId());

    return findByExistingUserProfile(userProfile)
        .orElseGet(() -> createNewUserProfile(userProfile));
  }

  private Optional<UserProfile> findByExistingUserProfile(UserProfile userProfile) {
    log.info("Checking if association between user and profile exists");

    var user = userService.findByIdOrThrowNotFound(userProfile.getApplicationUser().getId());
    var profile = profileService.findByIdOrThrowNotFound(userProfile.getProfile().getId());

    return userProfileRepository.findByApplicationUserAndProfile(user, profile);
  }

  private UserProfile createNewUserProfile(UserProfile userProfile) {
    log.info("Creating new association between user and profile");
    return userProfileRepository.save(userProfile);
  }

  public Page<UserProfile> getAllProfilesWithAssociatedUsers(Pageable pageable) {
    log.info("Finding user profiles by profile");

    return userProfileRepository.findAllWithApplicationUserAndProfile(pageable);
  }
}