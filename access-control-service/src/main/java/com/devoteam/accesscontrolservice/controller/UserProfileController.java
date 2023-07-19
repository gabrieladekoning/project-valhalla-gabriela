package com.devoteam.accesscontrolservice.controller;

import com.devoteam.accesscontrolservice.dto.UserProfileCreationRequest;
import com.devoteam.accesscontrolservice.dto.UserProfileCreationResponse;
import com.devoteam.accesscontrolservice.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/v1/user-profiles")
public class UserProfileController {
    private final UserProfileService userProfileService;

    @PostMapping
    @Operation(security = {@SecurityRequirement(name = "bearerAuth")},
            summary = "Create an association between application user and profile")
    public ResponseEntity<UserProfileCreationResponse> save(@Valid @RequestBody UserProfileCreationRequest creationRequest) {
        var userProfile = userProfileService.createOrRetrieveUserProfile(creationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userProfile);
    }
}