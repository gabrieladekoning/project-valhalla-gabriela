package com.devoteam.accesscontrolservice.controller;

import com.devoteam.accesscontrolservice.dto.ProfileCreationRequest;
import com.devoteam.accesscontrolservice.dto.ProfileCreationResponse;
import com.devoteam.accesscontrolservice.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/v1/profiles")
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping
    @Operation(security = {@SecurityRequirement(name = "bearerAuth")},
            summary = "Create a profile")
    @PreAuthorize("@checkPermissionService.isAccessAllowed('profile-service', 'profile', 'create')")
    public ResponseEntity<ProfileCreationResponse> save(@Valid @RequestBody ProfileCreationRequest creationRequest) {
        var profile = profileService.createOrRetrieveProfile(creationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(profile);
    }
}