package com.devoteam.accesscontrolservice.controller;

import com.devoteam.accesscontrolservice.dto.BusinessFunctionPermissionCreationRequest;
import com.devoteam.accesscontrolservice.dto.BusinessFunctionPermissionCreationResponse;
import com.devoteam.accesscontrolservice.dto.BusinessFunctionPermissionReadResponse;
import com.devoteam.accesscontrolservice.mapper.BusinessFunctionPermissionMapper;
import com.devoteam.accesscontrolservice.service.BusinessFunctionPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
@RequestMapping("/v1/business-function-permissions")
public class BusinessFunctionPermissionController {
    private final BusinessFunctionPermissionService businessFunctionPermissionService;

    @PostMapping
    @Operation(security = {@SecurityRequirement(name = "bearerAuth")},
            summary = "Create an association between business function and permission")
    @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'business-function-permission', 'create')")
    public ResponseEntity<BusinessFunctionPermissionCreationResponse> save(@Valid @RequestBody BusinessFunctionPermissionCreationRequest creationRequest) {
        var businessFunctionPermission = businessFunctionPermissionService.createOrRetrieveBusinessFunctionPermission(creationRequest);
        var creationResponse = BusinessFunctionPermissionMapper.INSTANCE.mapToBusinessFunctionPermissionCreationResponse(businessFunctionPermission);

        log.info("Created new / returned existing association between business function and permission '{}'", creationResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(creationResponse);
    }

    @GetMapping
    @Operation(summary = "Retrieve a paginated list of business function permissions by application name")
    @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'business-function-permission', 'read')")
    public ResponseEntity<Page<BusinessFunctionPermissionReadResponse>> listAll(@NotBlank @RequestParam(value = "applicationName") String applicationName, Pageable pageable) {
        var businessFunctionPermissions = businessFunctionPermissionService.listAllByApplicationName(applicationName, pageable);
        var businessFunctionReadResponses = businessFunctionPermissions.map(BusinessFunctionPermissionMapper.INSTANCE::mapToBusinessFunctionPermissionReadResponse);

        log.info("Listing business function permissions by application name: '{}'", applicationName);
        return ResponseEntity.status(HttpStatus.OK).body(businessFunctionReadResponses);
    }
}