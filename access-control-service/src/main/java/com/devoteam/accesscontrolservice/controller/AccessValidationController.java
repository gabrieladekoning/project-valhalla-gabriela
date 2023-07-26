package com.devoteam.accesscontrolservice.controller;

import com.devoteam.accesscontrolservice.service.AccessValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
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
    public ResponseEntity<Void> checkAccess(@Valid @RequestParam String applicationName,
                                            @Valid @RequestParam String functionName,
                                            @Valid @RequestParam String permission) {
        var userUuid = extractUserUuid();
        var isAllowed = accessValidationService.checkAccess(applicationName, functionName, permission, userUuid);

        var message = "User has " + (isAllowed ? "access" : "no access") + " to application: " + applicationName
                + ", function: " + functionName + " with permission: " + permission;
        log.info(message);

        return isAllowed ? ResponseEntity.status(HttpStatus.OK).build() : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    private UUID extractUserUuid() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = jwt.getClaim("sub");
        return UUID.fromString(userId);
    }
}