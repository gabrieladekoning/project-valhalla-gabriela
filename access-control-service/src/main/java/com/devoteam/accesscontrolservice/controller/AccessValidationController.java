package com.devoteam.accesscontrolservice.controller;

import com.devoteam.accesscontrolservice.service.AccessValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/access-validation")
public class AccessValidationController {
    private final AccessValidationService accessValidationService;

    @GetMapping("/validate")
    @Operation(security = {@SecurityRequirement(name = "bearerAuth")},
            summary = "Check if user has permission to access a specific resource")
    public ResponseEntity<Void> checkAccess(@NotBlank @RequestParam String applicationName,
                                            @NotBlank @RequestParam String functionName,
                                            @NotBlank @RequestParam String permission) {
        var isAllowed = accessValidationService.checkAccess(applicationName, functionName, permission);

        log.info("User has {} to application: {}, function: {} with permission: {}",
                isAllowed ? "access" : "no access", applicationName, functionName, permission);

        return isAllowed ? ResponseEntity.status(HttpStatus.OK).build() : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}