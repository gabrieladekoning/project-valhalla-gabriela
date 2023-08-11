package com.devoteam.accesscontrolservice.controller;

import com.devoteam.accesscontrolservice.dto.BusinessFunctionCreationRequest;
import com.devoteam.accesscontrolservice.dto.BusinessFunctionCreationResponse;
import com.devoteam.accesscontrolservice.mapper.BusinessFunctionMapper;
import com.devoteam.accesscontrolservice.service.BusinessFunctionService;
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
@RequestMapping("/v1/business-functions")
public class BusinessFunctionController {
    private final BusinessFunctionService businessFunctionService;

    @PostMapping
    @Operation(security = {@SecurityRequirement(name = "bearerAuth")},
            summary = "Create a business function")
    @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'business-function', 'create')")
    public ResponseEntity<BusinessFunctionCreationResponse> save(@Valid @RequestBody BusinessFunctionCreationRequest creationRequest) {
        var businessFunction = businessFunctionService.createOrRetrieveBusinessFunction(creationRequest);
        var creationResponse = BusinessFunctionMapper.INSTANCE.mapToBusinessFunctionResponse(businessFunction);

        log.info("Created new / returned existing business function '{}'", creationResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(creationResponse);
    }
}