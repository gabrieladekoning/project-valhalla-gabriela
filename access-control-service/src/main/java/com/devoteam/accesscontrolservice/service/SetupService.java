package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.SetupAllRequest;
import com.devoteam.accesscontrolservice.entity.ApplicationUser;
import com.devoteam.accesscontrolservice.entity.BusinessFunction;
import com.devoteam.accesscontrolservice.entity.BusinessFunctionPermission;
import com.devoteam.accesscontrolservice.entity.Permission;
import com.devoteam.accesscontrolservice.entity.Profile;
import com.devoteam.accesscontrolservice.entity.ProfileBusinessFunctionPermission;
import com.devoteam.accesscontrolservice.entity.UserProfile;
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

    var businessFunctionToUpsert = BusinessFunction.builder()
        .applicationName(setupAllRequest.getApplicationName())
        .functionName(setupAllRequest.getFunctionName())
        .build();

    return businessFunctionService.createOrRetrieveBusinessFunction(businessFunctionToUpsert);
  }

  private Permission upsertPermission(SetupAllRequest setupAllRequest) {
    log.info("Creating or retrieving permission");

    var permissionToUpsert = Permission.builder()
        .name(setupAllRequest.getPermissionName())
        .build();

    return permissionService.createOrRetrievePermission(permissionToUpsert);
  }

  private Profile upsertProfile(SetupAllRequest setupAllRequest) {
    log.info("Creating or retrieving profile");

    var profileToUpsert = Profile.builder()
        .name(setupAllRequest.getProfileName())
        .build();

    return profileService.createOrRetrieveProfile(profileToUpsert);
  }

  private BusinessFunctionPermission upsertBusinessFunctionPermission(BusinessFunction businessFunction,
      Permission permission) {
    log.info("Creating or retrieving association between business function and permission");

    var businessFunctionPermissionToUpsert = BusinessFunctionPermission.builder()
        .businessFunction(businessFunction)
        .permission(permission)
        .build();

    return businessFunctionPermissionService.createOrRetrieveBusinessFunctionPermission(
        businessFunctionPermissionToUpsert);
  }

  private void upsertProfileBusinessFunctionPermission(BusinessFunctionPermission businessFunctionPermission,
      Profile profile) {
    log.info("Creating or retrieving association between business function permission and profile'");

    var profileBusinessFunctionPermissionToUpsert = ProfileBusinessFunctionPermission.builder()
        .businessFunctionPermission(businessFunctionPermission)
        .profile(profile)
        .build();

    profileBusinessFunctionPermissionService.createOrRetrieveProfileBusinessFunctionPermission(
        profileBusinessFunctionPermissionToUpsert);
  }

  private void upsertUserProfile(ApplicationUser applicationUser, Profile profile) {
    log.info("Creating or retrieving association between user and profile");

    var userProfileToUpsert = UserProfile.builder()
        .applicationUser(applicationUser)
        .profile(profile)
        .build();

    userProfileService.createOrRetrieveUserProfile(userProfileToUpsert);
  }
}