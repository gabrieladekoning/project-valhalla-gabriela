package com.devoteam.accesscontrolservice.controller;

import com.devoteam.accesscontrolservice.dto.BusinessFunctionCreationRequest;
import com.devoteam.accesscontrolservice.dto.BusinessFunctionCreationResponse;
import com.devoteam.accesscontrolservice.service.BusinessFunctionService;
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
@RequestMapping("/v1/business-functions")
public class BusinessFunctionController {
    private final BusinessFunctionService businessFunctionService;

    @PostMapping
    public ResponseEntity<BusinessFunctionCreationResponse> save(@Valid @RequestBody BusinessFunctionCreationRequest businessFunction) {
        return ResponseEntity.status(HttpStatus.CREATED).body(businessFunctionService.saveBusinessFunction(businessFunction));
    }
}