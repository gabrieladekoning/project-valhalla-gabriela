package com.example.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/hello")
public class DemoController {

    @GetMapping("/create")
    @Operation(security = {@SecurityRequirement(name = "bearerAuth")}, summary = "Create an item")
    @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'permission', 'create')")
    public ResponseEntity<String> createEndpoint() {
        return ResponseEntity.ok("You can create!");
    }

    @GetMapping("/delete")
    @Operation(security = {@SecurityRequirement(name = "bearerAuth")}, summary = "Delete an item")
    @PreAuthorize("@checkPermissionService.isAccessAllowed('access-control-service', 'permission', 'delete')")
    public ResponseEntity<String> deleteEndpoint() {
        return ResponseEntity.ok("You can delete!");
    }
}