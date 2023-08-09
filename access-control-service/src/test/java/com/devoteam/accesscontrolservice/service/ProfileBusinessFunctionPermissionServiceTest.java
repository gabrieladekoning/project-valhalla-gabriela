package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.ProfileBusinessFunctionPermissionCreationRequest;
import com.devoteam.accesscontrolservice.entity.*;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.repository.ProfileBusinessFunctionPermissionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProfileBusinessFunctionPermissionServiceTest {
    @Mock
    private BusinessFunctionPermissionService businessFunctionPermissionService;

    @Mock
    private ProfileService profileService;

    @Mock
    private ProfileBusinessFunctionPermissionRepository profileBusinessFunctionPermissionRepository;

    @InjectMocks
    private ProfileBusinessFunctionPermissionService serviceUnderTest;

    private ProfileBusinessFunctionPermissionCreationRequest creationRequest;
    private BusinessFunctionPermission businessFunctionPermission;
    private Profile profile;
    private ProfileBusinessFunctionPermission profileBusinessFunctionPermission;

    @BeforeEach
    public void setup() {
        UUID profileBusinessFunctionPermissionId = UUID.randomUUID();
        UUID businessFunctionPermissionId = UUID.randomUUID();
        UUID businessFunctionId = UUID.randomUUID();
        UUID permissionId = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();

        creationRequest = ProfileBusinessFunctionPermissionCreationRequest.builder()
                .businessFunctionPermissionId(String.valueOf(businessFunctionPermissionId))
                .profileId(String.valueOf(profileId))
                .build();

        BusinessFunction businessFunction = BusinessFunction.builder()
                .id(businessFunctionId)
                .applicationName("Application name")
                .functionName("Function name")
                .build();

        Permission permission = Permission.builder()
                .id(permissionId)
                .name("Permission name")
                .build();

        businessFunctionPermission = BusinessFunctionPermission.builder()
                .id(businessFunctionPermissionId)
                .businessFunction(businessFunction)
                .permission(permission)
                .build();

        profile = Profile.builder()
                .id(profileId)
                .name("Profile name")
                .build();

        profileBusinessFunctionPermission = ProfileBusinessFunctionPermission.builder()
                .id(profileBusinessFunctionPermissionId)
                .businessFunctionPermission(businessFunctionPermission)
                .profile(profile)
                .build();
    }

    @DisplayName("Test saving a new ProfileBusinessFunctionPermission successfully")
    @Test
    void createOrRetrieveProfileBusinessFunctionPermission_SuccessfullySaved_WhenAssociationDoesNotAlreadyExist() {
        given(businessFunctionPermissionService.findByIdOrThrowNotFound(creationRequest.getBusinessFunctionPermissionId())).willReturn(businessFunctionPermission);
        given(profileService.findByIdOrThrowNotFound(creationRequest.getProfileId())).willReturn(profile);
        given(profileBusinessFunctionPermissionRepository.findByBusinessFunctionPermissionAndProfile(businessFunctionPermission, profile)).willReturn(Optional.empty());
        given(profileBusinessFunctionPermissionRepository.save(ArgumentMatchers.any())).willReturn(profileBusinessFunctionPermission);

        ProfileBusinessFunctionPermission response = serviceUnderTest.createOrRetrieveProfileBusinessFunctionPermission(creationRequest);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getId()).isEqualTo(profileBusinessFunctionPermission.getId());
    }

    @DisplayName("Test retrieving an existing ProfileBusinessFunctionPermission successfully")
    @Test
    void createOrRetrieveProfileBusinessFunctionPermission_SuccessfullyRetrieved_WhenAssociationAlreadyExists() {
        given(businessFunctionPermissionService.findByIdOrThrowNotFound(creationRequest.getBusinessFunctionPermissionId())).willReturn(businessFunctionPermission);
        given(profileService.findByIdOrThrowNotFound(creationRequest.getProfileId())).willReturn(profile);
        given(profileBusinessFunctionPermissionRepository.findByBusinessFunctionPermissionAndProfile(businessFunctionPermission, profile)).willReturn(Optional.of(profileBusinessFunctionPermission));

        var response = serviceUnderTest.createOrRetrieveProfileBusinessFunctionPermission(creationRequest);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getId()).isEqualTo(profileBusinessFunctionPermission.getId());
    }

    @DisplayName("Test handling when BusinessFunctionPermission is not found")
    @Test
    void createOrRetrieveProfileBusinessFunctionPermission_ThrowsResourceNotFoundException_WhenBusinessFunctionPermissionNotFound() {
        given(businessFunctionPermissionService.findByIdOrThrowNotFound(creationRequest.getBusinessFunctionPermissionId())).willThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> serviceUnderTest.createOrRetrieveProfileBusinessFunctionPermission(creationRequest));
    }

    @DisplayName("Test handling when Profile is not found")
    @Test
    void createOrRetrieveProfileBusinessFunctionPermission_ThrowsResourceNotFoundException_WhenProfileNotFound() {
        given(businessFunctionPermissionService.findByIdOrThrowNotFound(creationRequest.getBusinessFunctionPermissionId())).willReturn(businessFunctionPermission);
        given(profileService.findByIdOrThrowNotFound(creationRequest.getProfileId())).willThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> serviceUnderTest.createOrRetrieveProfileBusinessFunctionPermission(creationRequest));
    }
}