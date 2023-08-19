package com.devoteam.accesscontrolservice.controller;

import com.devoteam.accesscontrolservice.dto.ApplicationUserCreationRequest;
import com.devoteam.accesscontrolservice.dto.ApplicationUserCreationResponse;
import com.devoteam.accesscontrolservice.mapper.UserMapper;
import com.devoteam.accesscontrolservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<ApplicationUserCreationResponse> save(@Valid @RequestBody ApplicationUserCreationRequest creationRequest) {
        var savedUser = userService.saveApplicationUser(creationRequest);
        var creationResponse = userMapper.mapToApplicationUserCreationResponse(savedUser);

        log.debug("Saved user with ID: {}", creationResponse.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(creationResponse);
    }
}