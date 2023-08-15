package com.devoteam.accesscontrolservice.controller;

import com.devoteam.accesscontrolservice.dto.ProfileCreationRequest;
import com.devoteam.accesscontrolservice.dto.ProfileCreationResponse;
import com.devoteam.accesscontrolservice.dto.ProfileReadResponse;
import com.devoteam.accesscontrolservice.mapper.ProfileMapper;
import com.devoteam.accesscontrolservice.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/profiles")
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping
    @Operation(security = {@SecurityRequirement(name = "bearerAuth")},
            summary = "Create a profile")
    @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'profile', 'create')")
    public ResponseEntity<ProfileCreationResponse> save(@Valid @RequestBody ProfileCreationRequest creationRequest) {
        var profile = profileService.createOrRetrieveProfile(creationRequest);
        var creationResponse = ProfileMapper.INSTANCE.mapToProfileCreationResponse(profile);

        log.info("Created new / returned existing profile '{}", creationResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(creationResponse);
    }

    @GetMapping
    @Operation(summary = "Retrieve a paginated list of all profiles")
    @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'profile', 'read')")
    public ResponseEntity<Page<ProfileReadResponse>> listAll(Pageable pageable) {
        var profiles = profileService.listAll(pageable);
        var profileReadResponses = profiles.map(ProfileMapper.INSTANCE::mapToProfileReadResponse);

        log.info("Listing all profiles");
        return ResponseEntity.status(HttpStatus.OK).body(profileReadResponses);
    }
}