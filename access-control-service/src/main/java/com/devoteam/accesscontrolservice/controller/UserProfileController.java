package com.devoteam.accesscontrolservice.controller;

import com.devoteam.accesscontrolservice.dto.*;
import com.devoteam.accesscontrolservice.mapper.UserProfileMapper;
import com.devoteam.accesscontrolservice.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
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
@RequestMapping("/v1/user-profiles")
public class UserProfileController {
    private final UserProfileService userProfileService;
    private final UserProfileMapper userProfileMapper;

    @PostMapping
    @Operation(security = {@SecurityRequirement(name = "bearerAuth")},
            summary = "Create an association between application user and profile")
    @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'user-profile', 'create')")
    public ResponseEntity<UserProfileCreationResponse> save(@Valid @RequestBody UserProfileCreationRequest creationRequest) {
        var userProfile = userProfileService.createOrRetrieveUserProfile(creationRequest);
        var creationResponse = userProfileMapper.mapToUserProfileCreationResponse(userProfile);

        log.info("Created new / returned existing association between user and profile '{}'", creationResponse.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(creationResponse);
    }

    @GetMapping
    @Operation(summary = "Retrieve a paginated list of all users categorized by profile")
    @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'user-profile', 'read')")
    public ResponseEntity<Page<ProfileWithUsersReadResponse>> listAll(Pageable pageable) {
        var userProfiles = userProfileService.getAllProfilesWithAssociatedUsers(pageable);

        log.info("Listing all user profiles");
        return ResponseEntity.status(HttpStatus.OK).body(userProfiles);
    }
}