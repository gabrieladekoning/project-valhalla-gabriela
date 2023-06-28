package com.devoteam.accesscontrolservice.controller;

import com.devoteam.accesscontrolservice.dto.ApplicationUserDto;
import com.devoteam.accesscontrolservice.service.ApplicationUserService;
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
public class ApplicationUserController {
    private final ApplicationUserService applicationUserService;

    @PostMapping
    public ResponseEntity<ApplicationUserDto> save(@Valid @RequestBody ApplicationUserDto applicationUser) {
        ApplicationUserDto savedApplicationUser = applicationUserService.saveApplicationUser(applicationUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedApplicationUser);
    }
}