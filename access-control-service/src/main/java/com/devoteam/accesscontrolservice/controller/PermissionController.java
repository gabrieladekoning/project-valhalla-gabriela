package com.devoteam.accesscontrolservice.controller;

import com.devoteam.accesscontrolservice.dto.PermissionCreationRequest;
import com.devoteam.accesscontrolservice.dto.PermissionCreationResponse;
import com.devoteam.accesscontrolservice.service.PermissionService;
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
@RequestMapping("/v1/permissions")
public class PermissionController {
    private final PermissionService permissionService;

    @PostMapping
    @Operation(security = {@SecurityRequirement(name = "bearerAuth")},
            summary = "Create a permission")
    @PreAuthorize("@checkPermissionService.isAccessAllowed('permission-service', 'permission', 'create')")
    public ResponseEntity<PermissionCreationResponse> save(@Valid @RequestBody PermissionCreationRequest creationRequest) {
        var permission = permissionService.createOrRetrievePermission(creationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(permission);
    }
}