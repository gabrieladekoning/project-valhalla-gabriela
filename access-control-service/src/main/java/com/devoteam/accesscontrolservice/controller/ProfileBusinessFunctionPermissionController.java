package com.devoteam.accesscontrolservice.controller;

import com.devoteam.accesscontrolservice.dto.ProfileBusinessFunctionPermissionCreationRequest;
import com.devoteam.accesscontrolservice.dto.ProfileBusinessFunctionPermissionCreationResponse;
import com.devoteam.accesscontrolservice.mapper.ProfileBusinessFunctionPermissionMapper;
import com.devoteam.accesscontrolservice.service.ProfileBusinessFunctionPermissionService;
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
@RequestMapping("/v1/profile-business-function-permissions")
public class ProfileBusinessFunctionPermissionController {
    private final ProfileBusinessFunctionPermissionService profileBusinessFunctionPermissionService;

    @PostMapping
    @Operation(security = {@SecurityRequirement(name = "bearerAuth")},
            summary = "Create an association between business function permission and profile")
    @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'profile-business-function-permission', 'create')")
    public ResponseEntity<ProfileBusinessFunctionPermissionCreationResponse> save(@Valid @RequestBody ProfileBusinessFunctionPermissionCreationRequest creationRequest) {
        var profileBusinessFunctionPermission = profileBusinessFunctionPermissionService.createOrRetrieveProfileBusinessFunctionPermission(creationRequest);
        var creationResponse = ProfileBusinessFunctionPermissionMapper.INSTANCE.mapToProfileBusinessFunctionPermissionResponse(profileBusinessFunctionPermission);

        log.info("Created new / returned existing association between business function permission and profile '{}'", creationResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(creationResponse);
    }
}