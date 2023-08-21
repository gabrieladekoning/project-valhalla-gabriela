package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.UserIdAndEmailReadResponse;
import com.devoteam.accesscontrolservice.dto.ProfileWithUsersReadResponse;
import com.devoteam.accesscontrolservice.dto.UserProfileCreationRequest;
import com.devoteam.accesscontrolservice.entity.Profile;
import com.devoteam.accesscontrolservice.entity.UserProfile;
import com.devoteam.accesscontrolservice.mapper.ProfileMapper;
import com.devoteam.accesscontrolservice.mapper.UserProfileMapper;
import com.devoteam.accesscontrolservice.repository.UserProfileRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService {

  private final UserService userService;
  private final ProfileService profileService;
  private final UserProfileRepository userProfileRepository;
  private final UserProfileMapper userProfileMapper;


  public UserProfile createOrRetrieveUserProfile(UserProfileCreationRequest creationRequest) {
    log.info("Received a request to create an association between user ID: '{}' and profile ID: '{}'",
        creationRequest.getApplicationUserId(), creationRequest.getProfileId());

    return findByExistingUserProfile(creationRequest)
        .orElseGet(() -> createNewUserProfile(creationRequest));
  }

  private Optional<UserProfile> findByExistingUserProfile(UserProfileCreationRequest creationRequest) {
    log.info("Checking if association between user and profile exists '{}'", creationRequest);

    var user = userService.findByIdOrThrowNotFound(creationRequest.getApplicationUserId());
    var profile = profileService.findByIdOrThrowNotFound(creationRequest.getProfileId());

    return userProfileRepository.findByApplicationUserAndProfile(user, profile);
  }

  private UserProfile createNewUserProfile(UserProfileCreationRequest creationRequest) {
    log.info("Creating new association between user and profile '{}'", creationRequest);

    var userProfile = userProfileMapper.mapToUserProfile(creationRequest);
    return userProfileRepository.save(userProfile);
  }

  public Page<ProfileWithUsersReadResponse> getAllProfilesWithAssociatedUsers(Pageable pageable) {
    log.info("Fetching all profiles with associated users");

    var userProfilesPage = userProfileRepository.findAllWithApplicationUserAndProfile(pageable);
    var userProfiles = userProfilesPage.getContent();

    var profilesMap = groupProfilesWithUsers(userProfiles);

    var profileDTOs = profilesMap.entrySet().stream()
        .map(entry -> {
          Profile profile = entry.getKey();
          var users = entry.getValue();
          log.info("Mapped profile with ID: {} and name: {} to ProfileWithUsersReadResponse", profile.getId(),
              profile.getName());
          return userProfileMapper.mapToProfileWithUsersReadResponse(profile, users);
        })
        .toList();

    log.info("Returning a page of {} profiles with associated users", profileDTOs.size());

    return new PageImpl<>(profileDTOs, pageable, userProfilesPage.getTotalElements());
  }

  private Map<Profile, List<UserIdAndEmailReadResponse>> groupProfilesWithUsers(List<UserProfile> userProfiles) {
    return userProfiles.stream()
        .collect(Collectors.groupingBy(
            UserProfile::getProfile,
            Collectors.mapping(userProfileMapper::mapToUserReadResponse, Collectors.toList())
        ));
  }
}