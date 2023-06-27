package com.devoteam.accesscontrolservice.controller;

import com.devoteam.accesscontrolservice.dto.ApplicationUserDto;
import com.devoteam.accesscontrolservice.service.ApplicationUserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/users")
public class ApplicationUserController {
    private final ApplicationUserService applicationUserService;

    @PostMapping
    public ResponseEntity<ApplicationUserDto> save(@RequestBody ApplicationUserDto applicationUser) {
        ApplicationUserDto savedApplicationUser = applicationUserService.saveApplicationUser(applicationUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedApplicationUser);
    }
}