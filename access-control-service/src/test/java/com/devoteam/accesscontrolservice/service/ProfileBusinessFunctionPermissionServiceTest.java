package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.ProfileBusinessFunctionPermissionCreationRequest;
import com.devoteam.accesscontrolservice.dto.ProfileBusinessFunctionPermissionCreationResponse;
import com.devoteam.accesscontrolservice.entity.*;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.repository.BusinessFunctionPermissionRepository;
import com.devoteam.accesscontrolservice.repository.ProfileBusinessFunctionPermissionRepository;
import com.devoteam.accesscontrolservice.repository.ProfileRepository;
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
    private BusinessFunctionPermissionRepository businessFunctionPermissionRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private ProfileBusinessFunctionPermissionRepository profileBusinessFunctionPermissionRepository;

    @InjectMocks
    private ProfileBusinessFunctionPermissionService profileBusinessFunctionPermissionService;

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
        given(businessFunctionPermissionRepository.findById(businessFunctionPermission.getId())).willReturn(Optional.of(businessFunctionPermission));
        given(profileRepository.findById(profile.getId())).willReturn(Optional.of(profile));
        given(profileBusinessFunctionPermissionRepository.findByBusinessFunctionPermissionAndProfile(businessFunctionPermission, profile)).willReturn(Optional.empty());
        given(profileBusinessFunctionPermissionRepository.save(ArgumentMatchers.any())).willReturn(profileBusinessFunctionPermission);

        ProfileBusinessFunctionPermissionCreationResponse savedProfileBusinessFunctionPermission = profileBusinessFunctionPermissionService.createOrRetrieveProfileBusinessFunctionPermission(creationRequest);

        Assertions.assertThat(savedProfileBusinessFunctionPermission).hasNoNullFieldsOrProperties();
        Assertions.assertThat(savedProfileBusinessFunctionPermission.getId()).isEqualTo(profileBusinessFunctionPermission.getId().toString());
    }

    @DisplayName("Test retrieving an existing ProfileBusinessFunctionPermission successfully")
    @Test
    void createOrRetrieveProfileBusinessFunctionPermission_SuccessfullyRetrieved_WhenAssociationAlreadyExists() {
        given(businessFunctionPermissionRepository.findById(businessFunctionPermission.getId())).willReturn(Optional.of(businessFunctionPermission));
        given(profileRepository.findById(profile.getId())).willReturn(Optional.of(profile));
        given(profileBusinessFunctionPermissionRepository.findByBusinessFunctionPermissionAndProfile(businessFunctionPermission, profile)).willReturn(Optional.of(profileBusinessFunctionPermission));

        ProfileBusinessFunctionPermissionCreationResponse savedProfileBusinessFunctionPermission = profileBusinessFunctionPermissionService.createOrRetrieveProfileBusinessFunctionPermission(creationRequest);

        Assertions.assertThat(savedProfileBusinessFunctionPermission).isNotNull();
        Assertions.assertThat(savedProfileBusinessFunctionPermission.getId()).isEqualTo(profileBusinessFunctionPermission.getId().toString());
    }

    @DisplayName("Test handling when BusinessFunctionPermission is not found")
    @Test
    void createOrRetrieveProfileBusinessFunctionPermission_ThrowsResourceNotFoundException_WhenBusinessFunctionPermissionNotFound() {
        given(businessFunctionPermissionRepository.findById(businessFunctionPermission.getId())).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> profileBusinessFunctionPermissionService.createOrRetrieveProfileBusinessFunctionPermission(creationRequest));
    }

    @DisplayName("Test handling when Profile is not found")
    @Test
    void createOrRetrieveProfileBusinessFunctionPermission_ThrowsResourceNotFoundException_WhenProfileNotFound() {
        given(businessFunctionPermissionRepository.findById(businessFunctionPermission.getId())).willReturn(Optional.of(businessFunctionPermission));
        given(profileRepository.findById(profile.getId())).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> profileBusinessFunctionPermissionService.createOrRetrieveProfileBusinessFunctionPermission(creationRequest));
    }
}