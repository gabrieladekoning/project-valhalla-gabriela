package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.ProfileBusinessFunctionPermissionCreationRequest;
import com.devoteam.accesscontrolservice.entity.ProfileBusinessFunctionPermission;
import com.devoteam.accesscontrolservice.mapper.ProfileBusinessFunctionPermissionMapper;
import com.devoteam.accesscontrolservice.repository.ProfileBusinessFunctionPermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileBusinessFunctionPermissionService {
    private final BusinessFunctionPermissionService businessFunctionPermissionService;
    private final ProfileService profileService;
    private final ProfileBusinessFunctionPermissionRepository profileBusinessFunctionPermissionRepository;

    public ProfileBusinessFunctionPermission createOrRetrieveProfileBusinessFunctionPermission(ProfileBusinessFunctionPermissionCreationRequest creationRequest) {
        log.info("Received a request to create an association between business function ID: '{}' and permission ID: '{}'",
                creationRequest.getBusinessFunctionPermissionId(), creationRequest.getProfileId());

        return findByExistingProfileBusinessFunctionPermission(creationRequest)
                .orElseGet(() -> createNewProfileBusinessFunctionPermission(creationRequest));
    }

    private Optional<ProfileBusinessFunctionPermission> findByExistingProfileBusinessFunctionPermission(ProfileBusinessFunctionPermissionCreationRequest creationRequest) {
        log.info("Checking if association between profile and business function permission exists '{}'", creationRequest);

        var businessFunctionPermission = businessFunctionPermissionService.findByIdOrThrowNotFound(creationRequest.getBusinessFunctionPermissionId());
        var profile = profileService.findByIdOrThrowNotFound(creationRequest.getProfileId());

        return profileBusinessFunctionPermissionRepository.findByBusinessFunctionPermissionAndProfile(businessFunctionPermission, profile);
    }

    private ProfileBusinessFunctionPermission createNewProfileBusinessFunctionPermission(ProfileBusinessFunctionPermissionCreationRequest creationRequest) {
        log.info("Creating new association between profile and business function permission '{}'", creationRequest);

        var profileBusinessFunctionPermission = ProfileBusinessFunctionPermissionMapper.INSTANCE.mapToProfileBusinessFunctionPermission(creationRequest);

        return profileBusinessFunctionPermissionRepository.save(profileBusinessFunctionPermission);
    }
}