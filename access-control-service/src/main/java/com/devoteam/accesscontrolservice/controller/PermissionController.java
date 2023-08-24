package com.devoteam.accesscontrolservice.controller;

import com.devoteam.accesscontrolservice.dto.PermissionCreationRequest;
import com.devoteam.accesscontrolservice.dto.PermissionCreationResponse;
import com.devoteam.accesscontrolservice.dto.PermissionReadResponse;
import com.devoteam.accesscontrolservice.mapper.PermissionMapper;
import com.devoteam.accesscontrolservice.service.PermissionService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/permissions")
public class PermissionController {

  private final PermissionService permissionService;
  private final PermissionMapper permissionMapper;

  @PostMapping
  @Operation(security = {@SecurityRequirement(name = "bearerAuth")},
      summary = "Create a permission")
  @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'permission', 'create')")
  public ResponseEntity<PermissionCreationResponse> save(
      @Valid @RequestBody PermissionCreationRequest creationRequest) {
    var permissionToCreate = permissionMapper.mapToPermission(creationRequest);
    var permission = permissionService.createOrRetrievePermission(permissionToCreate);
    var creationResponse = permissionMapper.mapToPermissionCreationResponse(permission);

    log.info("Created new / returned existing permission '{}'", creationResponse);
    return ResponseEntity.status(HttpStatus.CREATED).body(creationResponse);
  }

  @GetMapping
  @Operation(summary = "Retrieve a paginated list of all permissions")
  @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'permission', 'read')")
  public ResponseEntity<Page<PermissionReadResponse>> listAll(Pageable pageable) {
    var permissions = permissionService.listAll(pageable);
    var permissionReadResponses = permissions.map(permissionMapper::mapToPermissionReadResponse);

    log.info("Listing all permissions");
    return ResponseEntity.status(HttpStatus.OK).body(permissionReadResponses);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Find permission by ID")
  @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'permission', 'read')")
  public ResponseEntity<PermissionReadResponse> findById(@PathVariable String id) {
    var permission = permissionService.findByIdOrThrowNotFound(UUID.fromString(id));
    var permissionReadResponse = permissionMapper.mapToPermissionReadResponse(permission);

    log.info("Returning permission found by ID: '{}'", id);
    return ResponseEntity.status(HttpStatus.OK).body(permissionReadResponse);
  }
}