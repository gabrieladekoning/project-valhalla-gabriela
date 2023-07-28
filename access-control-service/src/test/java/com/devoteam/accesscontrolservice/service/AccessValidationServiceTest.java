package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.entity.*;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

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
    private UUID userUuid;

    @BeforeEach
    void setup() {
        userUuid = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();

        applicationName = "Application name";
        functionName = "Function name";
        permissionName = "Permission name";

        applicationUser = ApplicationUser.builder()
                .id(userUuid)
                .build();

        profile = Profile.builder()
                .id(profileId)
                .build();

        userProfile = UserProfile.builder()
                .applicationUser(applicationUser)
                .profile(profile)
                .build();

        businessFunctionPermission = BusinessFunctionPermission.builder()
                .build();

        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", userUuid.toString())
                .build();

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        when(authentication.getPrincipal()).thenReturn(jwt);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @DisplayName("Test access validation when user, profile, business function permission exist and profile has permission")
    @Test
    void testAccessValidation_Success_WhenAllExistAndProfileHasPermission() {
        given(userRepository.findById(applicationUser.getId())).willReturn(Optional.of(applicationUser));
        given(userProfileRepository.findByApplicationUser(applicationUser)).willReturn(Optional.of(userProfile));
        given(businessFunctionRepository.findByApplicationNameAndFunctionName(applicationName, functionName)).willReturn(Optional.of(new BusinessFunction()));
        given(permissionRepository.findByName(permissionName)).willReturn(Optional.of(new Permission()));
        given(businessFunctionPermissionRepository.findByBusinessFunctionAndPermission(any(), any())).willReturn(Optional.of(businessFunctionPermission));
        given(profileBusinessFunctionPermissionRepository.existsByBusinessFunctionPermissionAndProfile(businessFunctionPermission, profile)).willReturn(true);

        boolean accessResult = accessValidationService.checkAccess(applicationName, functionName, permissionName);

        assertTrue(accessResult);
    }

    @DisplayName("Test access validation when user, profile, business function permission exist but profile does not have permission")
    @Test
    void testAccessValidation_Fail_WhenProfileDoesNotHavePermission() {
        given(userRepository.findById(applicationUser.getId())).willReturn(Optional.of(applicationUser));
        given(userProfileRepository.findByApplicationUser(applicationUser)).willReturn(Optional.of(userProfile));
        given(businessFunctionRepository.findByApplicationNameAndFunctionName(applicationName, functionName)).willReturn(Optional.of(new BusinessFunction()));
        given(permissionRepository.findByName(permissionName)).willReturn(Optional.of(new Permission()));
        given(businessFunctionPermissionRepository.findByBusinessFunctionAndPermission(any(), any())).willReturn(Optional.of(businessFunctionPermission));
        given(profileBusinessFunctionPermissionRepository.existsByBusinessFunctionPermissionAndProfile(businessFunctionPermission, profile)).willReturn(false);

        boolean accessResult = accessValidationService.checkAccess(applicationName, functionName, permissionName);

        assertFalse(accessResult);
    }

    @DisplayName("Test access validation when user cannot be found")
    @Test
    void testAccessValidation_Fail_WhenUserCannotBeFound() {
        given(userRepository.findById(applicationUser.getId())).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accessValidationService.checkAccess(applicationName, functionName, permissionName));
    }

    @DisplayName("Test access validation when user exists but does not have a profile")
    @Test
    void testAccessValidation_Fail_WhenUserHasNoProfile() {
        given(userRepository.findById(applicationUser.getId())).willReturn(Optional.of(applicationUser));
        given(userProfileRepository.findByApplicationUser(applicationUser)).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accessValidationService.checkAccess(applicationName, functionName, permissionName));
    }

    @DisplayName("Test access validation when business function permission does not exist")
    @Test
    void testAccessValidation_Fail_WhenBusinessFunctionPermissionDoesNotExist() {
        given(userRepository.findById(applicationUser.getId())).willReturn(Optional.of(applicationUser));
        given(userProfileRepository.findByApplicationUser(applicationUser)).willReturn(Optional.of(userProfile));
        given(businessFunctionRepository.findByApplicationNameAndFunctionName(applicationName, functionName)).willReturn(Optional.of(new BusinessFunction()));
        given(permissionRepository.findByName(permissionName)).willReturn(Optional.of(new Permission()));
        given(businessFunctionPermissionRepository.findByBusinessFunctionAndPermission(any(), any())).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accessValidationService.checkAccess(applicationName, functionName, permissionName));
    }
}