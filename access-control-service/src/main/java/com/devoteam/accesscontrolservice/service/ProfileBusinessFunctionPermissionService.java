package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.ProfileBusinessFunctionPermissionCreationRequest;
import com.devoteam.accesscontrolservice.dto.ProfileBusinessFunctionPermissionCreationResponse;
import com.devoteam.accesscontrolservice.entity.ProfileBusinessFunctionPermission;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.mapper.ProfileBusinessFunctionPermissionMapper;
import com.devoteam.accesscontrolservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileBusinessFunctionPermissionService {
    private final BusinessFunctionPermissionRepository businessFunctionPermissionRepository;
    private final ProfileRepository profileRepository;
    private final ProfileBusinessFunctionPermissionRepository profileBusinessFunctionPermissionRepository;

    public ProfileBusinessFunctionPermissionCreationResponse createOrRetrieveProfileBusinessFunctionPermission(ProfileBusinessFunctionPermissionCreationRequest creationRequest) {
        log.debug("Received a request to create an association between business function ID: {} and permission ID: {}",
                creationRequest.getBusinessFunctionPermissionId(), creationRequest.getProfileId());

        var existingAssociation = findByExistingProfileBusinessFunctionPermission(creationRequest);

        return existingAssociation
                .map(ProfileBusinessFunctionPermissionMapper.INSTANCE::mapToProfileBusinessFunctionPermissionResponse)
                .orElseGet(() -> createNewBusinessFunctionPermission(creationRequest));
    }

    private Optional<ProfileBusinessFunctionPermission> findByExistingProfileBusinessFunctionPermission(ProfileBusinessFunctionPermissionCreationRequest creationRequest) {
        var businessFunctionPermissionId = UUID.fromString(creationRequest.getBusinessFunctionPermissionId());
        var profileId = creationRequest.getProfileId();

        var businessFunctionPermission = businessFunctionPermissionRepository.findById(businessFunctionPermissionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Business function with ID: %s not found.".formatted(businessFunctionPermissionId)
                ));

        var profile = profileRepository.findById(UUID.fromString(profileId))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Profile with ID: %s not found.".formatted(profileId)
                ));
        return profileBusinessFunctionPermissionRepository.findByBusinessFunctionPermissionAndProfile(businessFunctionPermission, profile);
    }

    private ProfileBusinessFunctionPermissionCreationResponse createNewBusinessFunctionPermission(ProfileBusinessFunctionPermissionCreationRequest creationRequest) {
        var businessFunctionPermission = ProfileBusinessFunctionPermissionMapper.INSTANCE.mapToProfileBusinessFunctionPermission(creationRequest);
        var savedProfileBusinessFunctionPermission = profileBusinessFunctionPermissionRepository.save(businessFunctionPermission);

        log.debug("Created a new association between business function permission and profile with ID: {}", savedProfileBusinessFunctionPermission.getId());
        return ProfileBusinessFunctionPermissionMapper.INSTANCE.mapToProfileBusinessFunctionPermissionResponse(savedProfileBusinessFunctionPermission);
    }
}