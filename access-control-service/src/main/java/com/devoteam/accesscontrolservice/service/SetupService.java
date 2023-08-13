package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.*;
import com.devoteam.accesscontrolservice.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public void setupAll(SetupAllRequest setupAllRequest) {
        log.info("Received request to set up based on application name: '{}', function name: '{}', permission name: '{}', profile name: '{}', and email: '{}'",
                setupAllRequest.getApplicationName(), setupAllRequest.getFunctionName(), setupAllRequest.getPermissionName(), setupAllRequest.getProfileName(), setupAllRequest.getEmail());

        log.info("Finding user by email");
        var applicationUser = userService.findByEmailOrThrowNotFound(setupAllRequest.getEmail());

        log.info("Creating or retrieving business function");
        var businessFunction = upsertBusinessFunction(setupAllRequest);
        log.info("Creating or retrieving permission");
        var permission = upsertPermission(setupAllRequest);
        log.info("Creating or retrieving profile");
        var profile = upsertProfile(setupAllRequest);

        log.info("Creating or retrieving association between business function and permission");
        var businessFunctionPermission = upsertBusinessFunctionPermission(businessFunction, permission);
        log.info("Creating or retrieving association between business function permission and profile'");
        upsertProfileBusinessFunctionPermission(businessFunctionPermission, profile);
        log.info("Creating or retrieving association between user and profile");
        upsertUserProfile(applicationUser, profile);
    }


    private BusinessFunction upsertBusinessFunction(SetupAllRequest setupAllRequest) {
        var businessFunctionCreationRequest = new BusinessFunctionCreationRequest(
                setupAllRequest.getApplicationName(),
                setupAllRequest.getFunctionName());
        return businessFunctionService.createOrRetrieveBusinessFunction(businessFunctionCreationRequest);
    }

    private Permission upsertPermission(SetupAllRequest setupAllRequest) {
        var permissionCreationRequest = new PermissionCreationRequest(
                setupAllRequest.getPermissionName());
        return permissionService.createOrRetrievePermission(permissionCreationRequest);
    }

    private Profile upsertProfile(SetupAllRequest setupAllRequest) {
        var profileCreationRequest = new ProfileCreationRequest(
                setupAllRequest.getProfileName());
        return profileService.createOrRetrieveProfile(profileCreationRequest);
    }

    private BusinessFunctionPermission upsertBusinessFunctionPermission(BusinessFunction businessFunction, Permission permission) {
        var businessFunctionPermissionCreationRequest = new BusinessFunctionPermissionCreationRequest(String.valueOf(businessFunction.getId()), String.valueOf(permission.getId()));
        return businessFunctionPermissionService.createOrRetrieveBusinessFunctionPermission(businessFunctionPermissionCreationRequest);
    }

    private void upsertProfileBusinessFunctionPermission(BusinessFunctionPermission businessFunctionPermission, Profile profile) {
        var profileBusinessFunctionPermissionCreationRequest = new ProfileBusinessFunctionPermissionCreationRequest(String.valueOf(businessFunctionPermission.getId()), String.valueOf(profile.getId()));
        profileBusinessFunctionPermissionService.createOrRetrieveProfileBusinessFunctionPermission(profileBusinessFunctionPermissionCreationRequest);
    }

    private void upsertUserProfile(ApplicationUser applicationUser, Profile profile) {
        var userProfileCreationRequest = new UserProfileCreationRequest(String.valueOf(applicationUser.getId()), String.valueOf(profile.getId()));
        userProfileService.createOrRetrieveUserProfile(userProfileCreationRequest);
    }
}