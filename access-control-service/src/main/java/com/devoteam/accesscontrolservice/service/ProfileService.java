package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.entity.Profile;
import com.devoteam.accesscontrolservice.exception.DuplicateFieldException;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.repository.ProfileRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {

  private final ProfileRepository profileRepository;

  public Profile createOrRetrieveProfile(Profile profile) {
    log.info("Received a request to create a new profile with name: '{}'", profile.getName());

    return findExistingProfile(profile)
        .orElseGet(() -> createNewProfile(profile));
  }

  private Optional<Profile> findExistingProfile(Profile profile) {
    log.info("Checking if profile exists '{}'", profile.getName());

    return profileRepository.findByName(profile.getName());
  }

  private Profile createNewProfile(Profile profile) {
    log.info("Creating new profile '{}'", profile.getName());

    return profileRepository.save(profile);
  }

  public Profile findByIdOrThrowNotFound(UUID profileId) {
    log.info("Finding profile by ID: '{}'", profileId);

    return profileRepository.findById(profileId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Profile with ID: %s not found.".formatted(profileId)
        ));
  }

  public Page<Profile> listAll(Pageable pageable) {
    log.info("Finding all profiles");

    return profileRepository.findAll(pageable);
  }

  public void update(Profile profile) {
    var profileToUpdate = findByIdOrThrowNotFound(profile.getId());

    assertNameDoesNotExist(profile.getName(), profile.getId());

    log.info("Updating profile name from '{}' to '{}'", profileToUpdate.getName(), profile.getName());

    profileToUpdate.setName(profile.getName());
    profileRepository.save(profileToUpdate);
  }

  private void assertNameDoesNotExist(String name, UUID id) {
    log.info("Checking if profile name exists");
    profileRepository.findByName(name)
        .filter(profile -> !profile.getId().equals(id))
        .ifPresent(profile -> {
          throw new DuplicateFieldException(
              "Profile name is already registered: '%s'".formatted(profile.getName()));
        });
  }
}