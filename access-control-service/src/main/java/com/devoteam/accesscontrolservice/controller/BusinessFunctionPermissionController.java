package com.devoteam.accesscontrolservice.controller;

import com.devoteam.accesscontrolservice.dto.BusinessFunctionPermissionCreationRequest;
import com.devoteam.accesscontrolservice.dto.BusinessFunctionPermissionCreationResponse;
import com.devoteam.accesscontrolservice.mapper.BusinessFunctionPermissionMapper;
import com.devoteam.accesscontrolservice.service.BusinessFunctionPermissionService;
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
@RequestMapping("/v1/business-function-permissions")
public class BusinessFunctionPermissionController {
    private final BusinessFunctionPermissionService businessFunctionPermissionService;

    @PostMapping
    @Operation(security = {@SecurityRequirement(name = "bearerAuth")},
            summary = "Create an association between business function and permission")
    @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'business-function-permission', 'create')")
    public ResponseEntity<BusinessFunctionPermissionCreationResponse> save(@Valid @RequestBody BusinessFunctionPermissionCreationRequest creationRequest) {
        var businessFunctionPermission = businessFunctionPermissionService.createOrRetrieveBusinessFunctionPermission(creationRequest);
        var creationResponse = BusinessFunctionPermissionMapper.INSTANCE.mapToBusinessFunctionPermissionResponse(businessFunctionPermission);

        log.debug("Created a new association between business function and permission with ID: '{}'", creationResponse.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(creationResponse);
    }
}