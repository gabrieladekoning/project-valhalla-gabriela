package com.devoteam.accesscontrolservice.controller;

import com.devoteam.accesscontrolservice.dto.BusinessFunctionCreationRequest;
import com.devoteam.accesscontrolservice.dto.BusinessFunctionCreationResponse;
import com.devoteam.accesscontrolservice.dto.BusinessFunctionReadResponse;
import com.devoteam.accesscontrolservice.dto.BusinessFunctionUpdateRequest;
import com.devoteam.accesscontrolservice.mapper.BusinessFunctionMapper;
import com.devoteam.accesscontrolservice.service.BusinessFunctionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
  private final BusinessFunctionMapper businessFunctionMapper;

  @PostMapping
  @Operation(security = {@SecurityRequirement(name = "bearerAuth")},
      summary = "Create a business function")
  @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'business-function', 'create')")
  public ResponseEntity<BusinessFunctionCreationResponse> save(
      @Valid @RequestBody BusinessFunctionCreationRequest creationRequest) {
    var businessFunctionToCreate = businessFunctionMapper.mapToBusinessFunction(creationRequest);
    var businessFunction = businessFunctionService.createOrRetrieveBusinessFunction(businessFunctionToCreate);
    var creationResponse = businessFunctionMapper.mapToBusinessFunctionCreationResponse(businessFunction);

    log.info("Created new / returned existing business function '{}'", creationResponse);
    return ResponseEntity.status(HttpStatus.CREATED).body(creationResponse);
  }

  @GetMapping
  @Operation(summary = "Retrieve a paginated list of all business functions")
  @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'business-function', 'read')")
  public ResponseEntity<Page<BusinessFunctionReadResponse>> listAll(Pageable pageable) {
    var businessFunctions = businessFunctionService.listAll(pageable);
    var businessFunctionReadResponses = businessFunctions.map(
        businessFunctionMapper::mapToBusinessFunctionReadResponse);

    log.info("Listing all business functions");
    return ResponseEntity.status(HttpStatus.OK).body(businessFunctionReadResponses);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Find business function by ID")
  @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'business-function', 'read')")
  public ResponseEntity<BusinessFunctionReadResponse> findById(@PathVariable String id) {
    var businessFunction = businessFunctionService.findByIdOrThrowNotFound(UUID.fromString(id));
    var businessFunctionReadResponse = businessFunctionMapper.mapToBusinessFunctionReadResponse(businessFunction);

    log.info("Returning business function found by ID: '{}'", id);
    return ResponseEntity.status(HttpStatus.OK).body(businessFunctionReadResponse);
  }

  @PutMapping
  @Operation(summary = "Update business function")
//  @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'business-function', 'update')")
  public ResponseEntity<Void> updateById(@Valid @RequestBody BusinessFunctionUpdateRequest updateRequest) {
    var businessFunctionToUpdate = businessFunctionMapper.mapToBusinessFunction(updateRequest);
    businessFunctionService.update(businessFunctionToUpdate);

    log.info("Business function updated");
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}