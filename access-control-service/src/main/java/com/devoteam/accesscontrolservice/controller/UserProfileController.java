package com.devoteam.accesscontrolservice.controller;

import com.devoteam.accesscontrolservice.dto.ProfileWithUsersReadResponse;
import com.devoteam.accesscontrolservice.dto.UserProfileCreationRequest;
import com.devoteam.accesscontrolservice.dto.UserProfileCreationResponse;
import com.devoteam.accesscontrolservice.entity.UserProfile;
import com.devoteam.accesscontrolservice.mapper.UserProfileMapper;
import com.devoteam.accesscontrolservice.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/user-profiles")
public class UserProfileController {

  private final UserProfileService userProfileService;
  private final UserProfileMapper userProfileMapper;

  @PostMapping
  @Operation(security = {@SecurityRequirement(name = "bearerAuth")},
      summary = "Create an association between application user and profile")
  @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'user-profile', 'create')")
  public ResponseEntity<UserProfileCreationResponse> save(
      @Valid @RequestBody UserProfileCreationRequest creationRequest) {
    var userProfileToCreate = userProfileMapper.mapToUserProfile(creationRequest);
    var userProfile = userProfileService.createOrRetrieveUserProfile(userProfileToCreate);
    var creationResponse = userProfileMapper.mapToUserProfileCreationResponse(userProfile);

    log.info("Created new / returned existing association between user and profile '{}'", creationResponse.getId());
    return ResponseEntity.status(HttpStatus.CREATED).body(creationResponse);
  }

  @GetMapping
  @Operation(summary = "Retrieve a paginated list of all users categorized by profile")
  @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'user-profile', 'read')")
  public ResponseEntity<Page<ProfileWithUsersReadResponse>> listAll(Pageable pageable) {
    log.info("Fetching all profiles with associated users");

    var userProfilesPage = userProfileService.getAllProfilesWithAssociatedUsers(pageable);

    var profilesMap = userProfilesPage.getContent().stream()
        .collect(Collectors.groupingBy(UserProfile::getProfile));

    var profileWithUsersReadResponses = profilesMap.entrySet().stream().map(entry -> {
      var profile = entry.getKey();
      var users = entry.getValue().stream()
          .map(userProfileMapper::mapToUserReadResponse)
          .toList();
      return userProfileMapper.mapToProfileWithUsersReadResponse(profile, users);
    }).toList();

    log.info("Returning a page of {} profiles with associated users", profileWithUsersReadResponses.size());
    return ResponseEntity.status(HttpStatus.OK)
        .body(new PageImpl<>(profileWithUsersReadResponses, pageable, profileWithUsersReadResponses.size()));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete user profile by ID")
  @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'user-profile', 'delete')")
  public ResponseEntity<Void> deleteById(@PathVariable String id) {
    userProfileService.deleteById(UUID.fromString(id));

    log.info("Deleted user profile by ID: '{}'", id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}