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
        log.info("Setting up based on application name: '{}', function name: '{}', permission name: '{}', profile name: '{}', and email: '{}'",
                setupAllRequest.getApplicationName(), setupAllRequest.getFunctionName(), setupAllRequest.getPermissionName(), setupAllRequest.getProfileName(), setupAllRequest.getEmail());

        log.info("Finding user by email");
        var applicationUser = userService.findByEmailOrThrowNotFound(setupAllRequest.getEmail());

        log.info("Creating or retrieving business function");
        var businessFunction = upsertBusinessFunction(setupAllRequest);
        log.info("Creating or retrieving permission");
        var permission = upsertPermission(setupAllRequest);
        log.info("Creating or retrieving profile");
        var profile = upsertProfile(setupAllRequest);
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
}