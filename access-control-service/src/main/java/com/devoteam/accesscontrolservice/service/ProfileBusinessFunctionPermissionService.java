package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.entity.ProfileBusinessFunctionPermission;
import com.devoteam.accesscontrolservice.repository.ProfileBusinessFunctionPermissionRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileBusinessFunctionPermissionService {

  private final BusinessFunctionPermissionService businessFunctionPermissionService;
  private final ProfileService profileService;
  private final ProfileBusinessFunctionPermissionRepository profileBusinessFunctionPermissionRepository;

  public ProfileBusinessFunctionPermission createOrRetrieveProfileBusinessFunctionPermission(
      ProfileBusinessFunctionPermission profileBusinessFunctionPermission) {
    log.info("Received a request to create an association between business function and profile");

    return findByExistingProfileBusinessFunctionPermission(profileBusinessFunctionPermission)
        .orElseGet(() -> createNewProfileBusinessFunctionPermission(profileBusinessFunctionPermission));
  }

  private Optional<ProfileBusinessFunctionPermission> findByExistingProfileBusinessFunctionPermission(
      ProfileBusinessFunctionPermission profileBusinessFunctionPermission) {
    log.info("Checking if association between profile and business function permission exists");

    var businessFunctionPermission = businessFunctionPermissionService.findByIdOrThrowNotFound(
        profileBusinessFunctionPermission.getBusinessFunctionPermission().getId());
    var profile = profileService.findByIdOrThrowNotFound(profileBusinessFunctionPermission.getProfile().getId());

    return profileBusinessFunctionPermissionRepository.findByBusinessFunctionPermissionAndProfile(
        businessFunctionPermission, profile);
  }

  private ProfileBusinessFunctionPermission createNewProfileBusinessFunctionPermission(
      ProfileBusinessFunctionPermission profileBusinessFunctionPermission) {
    log.info("Creating new association between profile and business function permission");

    return profileBusinessFunctionPermissionRepository.save(profileBusinessFunctionPermission);
  }

  public Page<ProfileBusinessFunctionPermission> listAllByProfileName(String profileName, Pageable pageable) {
    log.info("Finding profile business function permissions by profile name: '{}'", profileName);

    return profileBusinessFunctionPermissionRepository.findByProfile_Name(profileName, pageable);
  }
}