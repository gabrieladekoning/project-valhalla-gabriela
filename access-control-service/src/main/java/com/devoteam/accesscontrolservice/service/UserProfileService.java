package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.UserIdAndEmailReadResponse;
import com.devoteam.accesscontrolservice.dto.ProfileWithUsersReadResponse;
import com.devoteam.accesscontrolservice.dto.UserProfileCreationRequest;
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
  private final ProfileMapper profileMapper;


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

    Page<UserProfile> userProfilesPage = userProfileRepository.findAllWithApplicationUserAndProfile(pageable);

    List<UserProfile> userProfiles = userProfilesPage.getContent();
    Map<String, List<UserProfile>> profilesMap = userProfiles.stream()
        .collect(Collectors.groupingBy(userProfile -> userProfile.getProfile().getId().toString()));

    List<ProfileWithUsersReadResponse> profileDTOs = new ArrayList<>();

    for (Map.Entry<String, List<UserProfile>> entry : profilesMap.entrySet()) {
      var profile = entry.getValue().get(0).getProfile();
      List<UserIdAndEmailReadResponse> users = entry.getValue().stream()
          .map(userProfileMapper::mapToUserReadResponse)
          .toList();
      profileDTOs.add(profileMapper.mapToProfileWithUsersReadResponse(profile, users));
      log.info("Mapped profile with ID: {} and name: {} to ProfileWithUsersReadResponse", profile.getId(), profile.getName());
    }
    log.info("Returning a page of {} profiles with associated users", profileDTOs.size());

    return new PageImpl<>(profileDTOs, pageable, userProfilesPage.getTotalElements());
  }
}
