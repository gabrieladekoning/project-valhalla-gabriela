package com.devoteam.accesscontrolservice.controller;

import com.devoteam.accesscontrolservice.dto.PermissionCreationRequest;
import com.devoteam.accesscontrolservice.dto.PermissionCreationResponse;
import com.devoteam.accesscontrolservice.dto.PermissionDto;
import com.devoteam.accesscontrolservice.entity.Permission;
import com.devoteam.accesscontrolservice.mapper.PermissionMapper;
import com.devoteam.accesscontrolservice.service.PermissionService;
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
@RequestMapping("/v1/permissions")
public class PermissionController {
    private final PermissionService permissionService;

    @PostMapping
    @Operation(security = {@SecurityRequirement(name = "bearerAuth")},
            summary = "Create a permission")
    @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'permission', 'create')")
    public ResponseEntity<PermissionCreationResponse> save(@Valid @RequestBody PermissionCreationRequest creationRequest) {
        var permission = permissionService.createOrRetrievePermission(creationRequest);
        var creationResponse = PermissionMapper.INSTANCE.mapToPermissionResponse(permission);

        log.info("Created new / returned existing permission '{}'", creationResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(creationResponse);
    }

    @GetMapping
    @Operation(summary = "Retrieve a paginated list of all permissions")
    @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'permission', 'read')")
    public ResponseEntity<Page<PermissionDto>> listAll(Pageable pageable) {
        var permissions = permissionService.listAll(pageable);
        var permissionDtos = permissions.map(PermissionMapper.INSTANCE::mapToPermissionDto);

        log.info("Listing all permissions");
        return ResponseEntity.status(HttpStatus.OK).body(permissionDtos);
    }
}