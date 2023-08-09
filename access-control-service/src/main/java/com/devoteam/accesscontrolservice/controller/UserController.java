package com.devoteam.accesscontrolservice.controller;

import com.devoteam.accesscontrolservice.dto.ApplicationUserCreationRequest;
import com.devoteam.accesscontrolservice.dto.ApplicationUserCreationResponse;
import com.devoteam.accesscontrolservice.mapper.UserMapper;
import com.devoteam.accesscontrolservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/v1/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create a user")
    public ResponseEntity<ApplicationUserCreationResponse> save(@Valid @RequestBody ApplicationUserCreationRequest creationRequest) {
        var savedUser = userService.saveApplicationUser(creationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.INSTANCE.mapToApplicationUserDto(savedUser));
    }
}