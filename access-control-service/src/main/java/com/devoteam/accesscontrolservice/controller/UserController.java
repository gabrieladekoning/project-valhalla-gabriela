package com.devoteam.accesscontrolservice.controller;

import com.devoteam.accesscontrolservice.dto.ApplicationUserCreationRequest;
import com.devoteam.accesscontrolservice.dto.ApplicationUserCreationResponse;
import com.devoteam.accesscontrolservice.dto.ApplicationUserReadResponse;
import com.devoteam.accesscontrolservice.mapper.UserMapper;
import com.devoteam.accesscontrolservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/users")
public class UserController {

  private final UserService userService;
  private final UserMapper userMapper;

  @PostMapping
  @Operation(summary = "Create a user")
  public ResponseEntity<ApplicationUserCreationResponse> save(
      @Valid @RequestBody ApplicationUserCreationRequest creationRequest) {
    var savedUser = userService.saveApplicationUser(creationRequest);
    var creationResponse = userMapper.mapToApplicationUserCreationResponse(savedUser);

    log.debug("Saved user with ID: {}", creationResponse.getId());
    return ResponseEntity.status(HttpStatus.CREATED).body(creationResponse);
  }

  //   return based on param, if no param return all?
  @GetMapping
  @Operation(summary = "Retrieve a paginated list of users through searching by first name, last name or email")
  @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'user', 'read')")
  public ResponseEntity<Page<ApplicationUserReadResponse>> listAll(
      @NotBlank @RequestParam(value = "firstName", required = false) String firstName,
      @NotBlank @RequestParam(value = "lastName", required = false) String lastName,
      @Email @RequestParam(value = "email", required = false) String email,
      Pageable pageable) {

    var applicationUsers = userService.searchAndListUsers(firstName,
        lastName, email, pageable);
    var applicationUserReadResponses = applicationUsers.map(userMapper::mapToApplicationUserReadResponse);

    log.info("Listing users");
    return ResponseEntity.status(HttpStatus.OK).body(applicationUserReadResponses);
  }
}