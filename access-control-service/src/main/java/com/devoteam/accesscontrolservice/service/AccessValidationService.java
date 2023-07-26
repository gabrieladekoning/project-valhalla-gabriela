package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.entity.*;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
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

    public boolean checkAccess(String applicationName, String functionName, String permissionName, UUID userUuid) {
        ApplicationUser applicationUser = findApplicationUser(userUuid);
        UserProfile userProfile = findUserProfile(applicationUser);
        Profile profile = userProfile.getProfile();
        BusinessFunctionPermission businessFunctionPermission = findBusinessFunctionPermission(applicationName, functionName, permissionName);
        return checkProfilePermission(businessFunctionPermission, profile);
    }

    private ApplicationUser findApplicationUser(UUID userUuid) {
        return userRepository.findById(userUuid)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID: %s not found.".formatted(userUuid)));
    }

    private UserProfile findUserProfile(ApplicationUser applicationUser) {
        return userProfileRepository.findByApplicationUser(applicationUser)
                .orElseThrow(() -> new ResourceNotFoundException("Profile associated with user ID: %s not found.".formatted(applicationUser.getApplicationUserId())));
    }

    private BusinessFunctionPermission findBusinessFunctionPermission(String applicationName, String functionName, String permissionName) {
        // todo: write neater
        Optional<BusinessFunction> optionalBusinessFunction = businessFunctionRepository.findByApplicationNameAndFunctionName(applicationName, functionName);
        if (optionalBusinessFunction.isEmpty()) {
            throw new ResourceNotFoundException("BusinessFunction not found with applicationName: " + applicationName + " and functionName: " + functionName);
        }
        BusinessFunction businessFunction = optionalBusinessFunction.get();

        Optional<Permission> optionalPermission = permissionRepository.findByName(permissionName);
        if (optionalPermission.isEmpty()) {
            throw new ResourceNotFoundException("Permission not found with applicationName: " + permissionName);
        }
        Permission permission = optionalPermission.get();
        return businessFunctionPermissionRepository.findByBusinessFunctionAndPermission(businessFunction, permission)
                .orElseThrow(() -> new ResourceNotFoundException("Business function association with permission not found"));
    }

    private boolean checkProfilePermission(BusinessFunctionPermission businessFunctionPermission, Profile profile) {
        return profileBusinessFunctionPermissionRepository.existsByBusinessFunctionPermissionAndProfile(businessFunctionPermission, profile);
    }
}