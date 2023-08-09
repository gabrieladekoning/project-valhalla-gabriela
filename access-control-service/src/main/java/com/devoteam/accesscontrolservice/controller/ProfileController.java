package com.devoteam.accesscontrolservice.controller;

import com.devoteam.accesscontrolservice.dto.ProfileCreationRequest;
import com.devoteam.accesscontrolservice.dto.ProfileCreationResponse;
import com.devoteam.accesscontrolservice.mapper.ProfileMapper;
import com.devoteam.accesscontrolservice.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping
    @Operation(security = {@SecurityRequirement(name = "bearerAuth")},
            summary = "Create a profile")
    @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'profile', 'create')")
    public ResponseEntity<ProfileCreationResponse> save(@Valid @RequestBody ProfileCreationRequest creationRequest) {
        var profile = profileService.createOrRetrieveProfile(creationRequest);
        var creationResponse = ProfileMapper.INSTANCE.mapToProfileResponse(profile);

        log.debug("Created a new profile with ID: {}", creationResponse.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(creationResponse);
    }
}