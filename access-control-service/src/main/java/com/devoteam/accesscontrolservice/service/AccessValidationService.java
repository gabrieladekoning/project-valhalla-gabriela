package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.entity.ApplicationUser;
import com.devoteam.accesscontrolservice.entity.BusinessFunctionPermission;
import com.devoteam.accesscontrolservice.entity.Profile;
import com.devoteam.accesscontrolservice.entity.UserProfile;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccessValidationService {
    private final UserRepository userRepository;
    private final BusinessFunctionRepository businessFunctionRepository;
    private final PermissionRepository permissionRepository;
    private final UserProfileRepository userProfileRepository;
    private final BusinessFunctionPermissionRepository businessFunctionPermissionRepository;
    private final ProfileBusinessFunctionPermissionRepository profileBusinessFunctionPermissionRepository;

    public boolean checkAccess(String applicationName, String functionName, String permissionName) {
        var userUuid = extractUserUuid();
        var applicationUser = findApplicationUser(userUuid);
        var userProfile = findUserProfile(applicationUser);
        var profile = userProfile.getProfile();
        var businessFunctionPermission = findBusinessFunctionPermission(applicationName, functionName, permissionName);
        return checkProfilePermission(businessFunctionPermission, profile);
    }

    public UUID extractUserUuid() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = jwt.getClaim("sub");
        return UUID.fromString(userId);
    }

    private ApplicationUser findApplicationUser(UUID userUuid) {
        return userRepository.findById(userUuid)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID: %s not found.".formatted(userUuid)));
    }

    private UserProfile findUserProfile(ApplicationUser applicationUser) {
        return userProfileRepository.findByApplicationUser(applicationUser)
                .orElseThrow(() -> new ResourceNotFoundException("Profile associated with user ID: %s not found.".formatted(applicationUser.getId())));
    }

    private BusinessFunctionPermission findBusinessFunctionPermission(String applicationName, String functionName, String permissionName) {
        var businessFunction = businessFunctionRepository.findByApplicationNameAndFunctionName(applicationName, functionName)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ("Business function with application name: '%s' and function name: '%s' not found".formatted(applicationName, functionName))));

        var permission = permissionRepository.findByName(permissionName)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ("Permission with name: '%s' not found".formatted(permissionName))));

        return businessFunctionPermissionRepository.findByBusinessFunctionAndPermission(businessFunction, permission)
                .orElseThrow(() -> new ResourceNotFoundException("Association between business function: '%s' and permission: '%s' not found".formatted(businessFunction.getFunctionName(), permission.getName())));
    }

    private boolean checkProfilePermission(BusinessFunctionPermission businessFunctionPermission, Profile profile) {
        return profileBusinessFunctionPermissionRepository.existsByBusinessFunctionPermissionAndProfile(businessFunctionPermission, profile);
    }
}