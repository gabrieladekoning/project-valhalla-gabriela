package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.entity.*;
import com.devoteam.accesscontrolservice.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
class AccessValidationServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private BusinessFunctionRepository businessFunctionRepository;
    @Mock
    private PermissionRepository permissionRepository;
    @Mock
    private UserProfileRepository userProfileRepository;
    @Mock
    private BusinessFunctionPermissionRepository businessFunctionPermissionRepository;
    @Mock
    private ProfileBusinessFunctionPermissionRepository profileBusinessFunctionPermissionRepository;
    @InjectMocks
    private AccessValidationService accessValidationService;
    private ApplicationUser applicationUser;
    private UserProfile userProfile;
    private BusinessFunctionPermission businessFunctionPermission;
    private Profile profile;
    private String applicationName;
    private String functionName;
    private String permissionName;

    @BeforeEach
    void setup() {
        UUID userId = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();

        applicationName = "Application name";
        functionName = "Function name";
        permissionName = "Permission name";

        applicationUser = ApplicationUser.builder()
                .applicationUserId(userId)
                .build();

        profile = Profile.builder()
                .profileId(profileId)
                .build();

        userProfile = UserProfile.builder()
                .applicationUser(applicationUser)
                .profile(profile)
                .build();

        businessFunctionPermission = BusinessFunctionPermission.builder()
                .build();
    }

    @DisplayName("Test access validation when user, profile, business function permission exist and profile has permission")
    @Test
    void testAccessValidation_Success_WhenAllExistAndProfileHasPermission() {
        given(userRepository.findById(applicationUser.getApplicationUserId())).willReturn(Optional.of(applicationUser));
        given(userProfileRepository.findByApplicationUser(applicationUser)).willReturn(Optional.of(userProfile));
        given(businessFunctionRepository.findByApplicationNameAndFunctionName(applicationName, functionName)).willReturn(Optional.of(new BusinessFunction()));
        given(permissionRepository.findByName(permissionName)).willReturn(Optional.of(new Permission()));
        given(businessFunctionPermissionRepository.findByBusinessFunctionAndPermission(any(), any())).willReturn(Optional.of(businessFunctionPermission));
        given(profileBusinessFunctionPermissionRepository.existsByBusinessFunctionPermissionAndProfile(businessFunctionPermission, profile)).willReturn(true);

        boolean accessResult = accessValidationService.checkAccess(applicationName, functionName, permissionName, applicationUser.getApplicationUserId());

        assertTrue(accessResult);
    }

    @DisplayName("Test access validation when user, profile, business function permission exist but profile does not have permission")
    @Test
    void testAccessValidation_Fail_WhenProfileDoesNotHavePermission() {
        given(userRepository.findById(applicationUser.getApplicationUserId())).willReturn(Optional.of(applicationUser));
        given(userProfileRepository.findByApplicationUser(applicationUser)).willReturn(Optional.of(userProfile));
        given(businessFunctionRepository.findByApplicationNameAndFunctionName(applicationName, functionName)).willReturn(Optional.of(new BusinessFunction()));
        given(permissionRepository.findByName(permissionName)).willReturn(Optional.of(new Permission()));
        given(businessFunctionPermissionRepository.findByBusinessFunctionAndPermission(any(), any())).willReturn(Optional.of(businessFunctionPermission));
        given(profileBusinessFunctionPermissionRepository.existsByBusinessFunctionPermissionAndProfile(businessFunctionPermission, profile)).willReturn(false);

        boolean accessResult = accessValidationService.checkAccess(applicationName, functionName, permissionName, applicationUser.getApplicationUserId());

        assertFalse(accessResult);
    }
}