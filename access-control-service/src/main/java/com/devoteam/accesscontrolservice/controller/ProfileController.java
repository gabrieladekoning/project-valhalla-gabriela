package com.devoteam.accesscontrolservice.controller;

import com.devoteam.accesscontrolservice.dto.ProfileCreationRequest;
import com.devoteam.accesscontrolservice.dto.ProfileCreationResponse;
import com.devoteam.accesscontrolservice.dto.ProfileReadResponse;
import com.devoteam.accesscontrolservice.dto.ProfileUpdateRequest;
import com.devoteam.accesscontrolservice.mapper.ProfileMapper;
import com.devoteam.accesscontrolservice.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/profiles")
public class ProfileController {

  private final ProfileService profileService;
  private final ProfileMapper profileMapper;

  @PostMapping
  @Operation(security = {@SecurityRequirement(name = "bearerAuth")},
      summary = "Create a profile")
  @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'profile', 'create')")
  public ResponseEntity<ProfileCreationResponse> save(@Valid @RequestBody ProfileCreationRequest creationRequest) {
    var profileToCreate = profileMapper.mapToProfile(creationRequest);
    var profile = profileService.createOrRetrieveProfile(profileToCreate);
    var creationResponse = profileMapper.mapToProfileCreationResponse(profile);

    log.info("Created new / returned existing profile '{}", creationResponse);
    return ResponseEntity.status(HttpStatus.CREATED).body(creationResponse);
  }

  @GetMapping
  @Operation(summary = "Retrieve a paginated list of all profiles")
  @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'profile', 'read')")
  public ResponseEntity<Page<ProfileReadResponse>> listAll(Pageable pageable) {
    var profiles = profileService.listAll(pageable);
    var profileReadResponses = profiles.map(profileMapper::mapToProfileReadResponse);

    log.info("Listing all profiles");
    return ResponseEntity.status(HttpStatus.OK).body(profileReadResponses);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Find profile by ID")
  @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'profile', 'read')")
  public ResponseEntity<ProfileReadResponse> findById(@PathVariable String id) {
    var profile = profileService.findByIdOrThrowNotFound(UUID.fromString(id));
    var profileReadResponse = profileMapper.mapToProfileReadResponse(profile);

    log.info("Returning profile found by ID: '{}'", id);
    return ResponseEntity.status(HttpStatus.OK).body(profileReadResponse);
  }

  @PutMapping
  @Operation(summary = "Update profile")
  @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'profile', 'update')")
  public ResponseEntity<Void> updateById(@Valid @RequestBody ProfileUpdateRequest updateRequest) {
    var profileToUpdate = profileMapper.mapToProfile(updateRequest);
    profileService.update(profileToUpdate);

    log.info("Profile updated");
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete profile by ID")
  @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'profile', 'delete')")
  public ResponseEntity<Void> deleteById(@PathVariable String id) {
    profileService.deleteById(UUID.fromString(id));

    log.info("Deleted profile by ID: '{}'", id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @DeleteMapping("/{id}/cascade")
  @Operation(summary = "Cascade delete profile by ID")
  @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'profile', 'delete')")
  public ResponseEntity<Void> cascadeDeleteById(@PathVariable String id) {
    profileService.cascadeDeleteById(UUID.fromString(id));

    log.info("Cascade deleted profile by ID: '{}'", id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}