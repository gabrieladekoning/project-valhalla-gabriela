package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.SetupAllRequest;
import com.devoteam.accesscontrolservice.entity.*;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SetupServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private BusinessFunctionService businessFunctionService;
    @Mock
    private PermissionService permissionService;
    @Mock
    private ProfileService profileService;
    @Mock
    private BusinessFunctionPermissionService businessFunctionPermissionService;
    @Mock
    private ProfileBusinessFunctionPermissionService profileBusinessFunctionPermissionService;
    @Mock
    private UserProfileService userProfileService;

    @InjectMocks
    private SetupService setupService;

    private SetupAllRequest setupAllRequest;
    private ApplicationUser applicationUser;
    private BusinessFunction businessFunction;
    private Permission permission;
    private Profile profile;
    private BusinessFunctionPermission businessFunctionPermission;

    @BeforeEach
    void setUp() {
        applicationUser = ApplicationUser.builder()
                .id(UUID.randomUUID())
                .email("test@email.com").build();
        businessFunction = BusinessFunction.builder()
                .id(UUID.randomUUID())
                .applicationName("Application name")
                .functionName("Function name")
                .build();
        permission = Permission.builder()
                .id(UUID.randomUUID())
                .name("Permission name").build();
        profile = Profile.builder()
                .id(UUID.randomUUID())
                .name("Profile name").build();
        businessFunctionPermission = BusinessFunctionPermission.builder()
                .id(UUID.randomUUID())
                .businessFunction(businessFunction)
                .permission(permission).build();

        setupAllRequest = SetupAllRequest.builder()
                .applicationName(businessFunction.getApplicationName())
                .functionName(businessFunction.getFunctionName())
                .permissionName(permission.getName())
                .profileName(profile.getName())
                .email(applicationUser.getEmail())
                .build();
    }

    @DisplayName("setupAll successfully upserts application, function, permission, profile")
    @Test
    void setupAll_SuccessfullyUpsertsAll() {
        given(userService.findByEmailOrThrowNotFound(setupAllRequest.getEmail())).willReturn(applicationUser);
        given(businessFunctionService.createOrRetrieveBusinessFunction(any())).willReturn(businessFunction);
        given(permissionService.createOrRetrievePermission(any())).willReturn(permission);
        given(profileService.createOrRetrieveProfile(any())).willReturn(profile);
        given(businessFunctionPermissionService.createOrRetrieveBusinessFunctionPermission(any())).willReturn(businessFunctionPermission);

        setupService.setupAll(setupAllRequest);

        verify(userService).findByEmailOrThrowNotFound(setupAllRequest.getEmail());
        verify(businessFunctionService).createOrRetrieveBusinessFunction(any());
        verify(permissionService).createOrRetrievePermission(any());
        verify(profileService).createOrRetrieveProfile(any());
        verify(businessFunctionPermissionService).createOrRetrieveBusinessFunctionPermission(any());
        verify(profileBusinessFunctionPermissionService).createOrRetrieveProfileBusinessFunctionPermission(any());
        verify(userProfileService).createOrRetrieveUserProfile(any());
    }

    @DisplayName("Handling when user is not found")
    @Test
    void setupAll_UserNotFound_ThrowsResourceNotFoundException() {
        given(userService.findByEmailOrThrowNotFound(setupAllRequest.getEmail()))
                .willThrow(new ResourceNotFoundException("User with email: %s not found.".formatted(setupAllRequest.getEmail())));

        assertThrows(ResourceNotFoundException.class, () -> setupService.setupAll(setupAllRequest));

        then(businessFunctionService).shouldHaveNoInteractions();
        then(permissionService).shouldHaveNoInteractions();
        then(profileService).shouldHaveNoInteractions();
        then(profileBusinessFunctionPermissionService).shouldHaveNoInteractions();
        then(userProfileService).shouldHaveNoInteractions();
    }
}