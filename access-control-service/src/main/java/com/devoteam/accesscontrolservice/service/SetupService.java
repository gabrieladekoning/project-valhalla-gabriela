package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.*;
import com.devoteam.accesscontrolservice.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SetupService {

  private final UserService userService;
  private final BusinessFunctionService businessFunctionService;
  private final PermissionService permissionService;
  private final ProfileService profileService;
  private final BusinessFunctionPermissionService businessFunctionPermissionService;
  private final ProfileBusinessFunctionPermissionService profileBusinessFunctionPermissionService;
  private final UserProfileService userProfileService;

  @Transactional
  public void setupAll(SetupAllRequest setupAllRequest) {
    log.info("Received request to set up based on the following: '{}'", setupAllRequest);

    var applicationUser = userService.findByEmailOrThrowNotFound(setupAllRequest.getEmail());

    var businessFunction = upsertBusinessFunction(setupAllRequest);
    var permission = upsertPermission(setupAllRequest);
    var profile = upsertProfile(setupAllRequest);

    var businessFunctionPermission = upsertBusinessFunctionPermission(businessFunction, permission);
    upsertProfileBusinessFunctionPermission(businessFunctionPermission, profile);
    upsertUserProfile(applicationUser, profile);
  }

  private BusinessFunction upsertBusinessFunction(SetupAllRequest setupAllRequest) {
    log.info("Creating or retrieving business function");
    var businessFunctionCreationRequest = new BusinessFunctionCreationRequest(
        setupAllRequest.getApplicationName(),
        setupAllRequest.getFunctionName());
    return businessFunctionService.createOrRetrieveBusinessFunction(businessFunctionCreationRequest);
  }

  private Permission upsertPermission(SetupAllRequest setupAllRequest) {
    log.info("Creating or retrieving permission");
    var permissionCreationRequest = new PermissionCreationRequest(
        setupAllRequest.getPermissionName());
    return permissionService.createOrRetrievePermission(permissionCreationRequest);
  }

  private Profile upsertProfile(SetupAllRequest setupAllRequest) {
    log.info("Creating or retrieving profile");
    var profileCreationRequest = new ProfileCreationRequest(
        setupAllRequest.getProfileName());
    return profileService.createOrRetrieveProfile(profileCreationRequest);
  }

  private BusinessFunctionPermission upsertBusinessFunctionPermission(BusinessFunction businessFunction,
      Permission permission) {
    log.info("Creating or retrieving association between business function and permission");
    var businessFunctionPermissionCreationRequest = new BusinessFunctionPermissionCreationRequest(
        String.valueOf(businessFunction.getId()), String.valueOf(permission.getId()));
    return businessFunctionPermissionService.createOrRetrieveBusinessFunctionPermission(
        businessFunctionPermissionCreationRequest);
  }

  private void upsertProfileBusinessFunctionPermission(BusinessFunctionPermission businessFunctionPermission,
      Profile profile) {
    log.info("Creating or retrieving association between business function permission and profile'");
    var profileBusinessFunctionPermissionCreationRequest = new ProfileBusinessFunctionPermissionCreationRequest(
        String.valueOf(businessFunctionPermission.getId()), String.valueOf(profile.getId()));
    profileBusinessFunctionPermissionService.createOrRetrieveProfileBusinessFunctionPermission(
        profileBusinessFunctionPermissionCreationRequest);
  }

  private void upsertUserProfile(ApplicationUser applicationUser, Profile profile) {
    log.info("Creating or retrieving association between user and profile");
    var userProfileCreationRequest = new UserProfileCreationRequest(String.valueOf(applicationUser.getId()),
        String.valueOf(profile.getId()));
    userProfileService.createOrRetrieveUserProfile(userProfileCreationRequest);
  }
}