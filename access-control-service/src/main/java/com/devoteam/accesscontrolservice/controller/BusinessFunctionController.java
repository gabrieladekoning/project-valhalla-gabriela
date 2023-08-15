package com.devoteam.accesscontrolservice.controller;

import com.devoteam.accesscontrolservice.dto.BusinessFunctionCreationRequest;
import com.devoteam.accesscontrolservice.dto.BusinessFunctionCreationResponse;
import com.devoteam.accesscontrolservice.dto.BusinessFunctionReadResponse;
import com.devoteam.accesscontrolservice.mapper.BusinessFunctionMapper;
import com.devoteam.accesscontrolservice.service.BusinessFunctionService;
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
@RequestMapping("/v1/business-functions")
public class BusinessFunctionController {
    private final BusinessFunctionService businessFunctionService;

    @PostMapping
    @Operation(security = {@SecurityRequirement(name = "bearerAuth")},
            summary = "Create a business function")
    @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'business-function', 'create')")
    public ResponseEntity<BusinessFunctionCreationResponse> save(@Valid @RequestBody BusinessFunctionCreationRequest creationRequest) {
        var businessFunction = businessFunctionService.createOrRetrieveBusinessFunction(creationRequest);
        var creationResponse = BusinessFunctionMapper.INSTANCE.mapToBusinessFunctionCreationResponse(businessFunction);

        log.info("Created new / returned existing business function '{}'", creationResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(creationResponse);
    }

    @GetMapping
    @Operation(summary = "Retrieve a paginated list of all businessFunctions")
    @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'businessFunction', 'read')")
    public ResponseEntity<Page<BusinessFunctionReadResponse>> listAll(Pageable pageable) {
        var businessFunctions = businessFunctionService.listAll(pageable);
        var businessFunctionReadResponses = businessFunctions.map(BusinessFunctionMapper.INSTANCE::mapToBusinessFunctionReadResponse);

        log.info("Listing all businessFunctions");
        return ResponseEntity.status(HttpStatus.OK).body(businessFunctionReadResponses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find businessFunction by ID")
    @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'businessFunction', 'read')")
    public ResponseEntity<BusinessFunctionReadResponse> findById(@PathVariable String id) {
        var businessFunction = businessFunctionService.findByIdOrThrowNotFound(id);
        var businessFunctionReadResponse = BusinessFunctionMapper.INSTANCE.mapToBusinessFunctionReadResponse(businessFunction);

        log.info("Returning businessFunction found by ID: '{}'", id);
        return ResponseEntity.status(HttpStatus.OK).body(businessFunctionReadResponse);
    }
}