package com.devoteam.accesscontrolservice.controller;

import com.devoteam.accesscontrolservice.dto.SetupAllRequest;
import com.devoteam.accesscontrolservice.service.AccessValidationService;
import com.devoteam.accesscontrolservice.service.SetupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/setup")
public class SetupController {
    private final SetupService setupService;

    @PostMapping("/setup-all")
    @Operation(security = {@SecurityRequirement(name = "bearerAuth")},
            summary = "Endpoint to setup business function, permission, profile, association between business function and permission, " +
                    "association between profile and business function permission according to upsert approach")
    public ResponseEntity<Void> setupAll(@Valid @RequestBody SetupAllRequest setupAllRequest) {
        setupService.setupAll(setupAllRequest);

        log.info("Upsert performed with the following data: '{}'", setupAllRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}