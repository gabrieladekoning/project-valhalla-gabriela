package com.devoteam.accesscontrolservice.controller;

import com.devoteam.accesscontrolservice.service.BusinessFunctionPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/v1/business-function-permission")
public class BusinessFunctionPermissionController {
    private final BusinessFunctionPermissionService businessFunctionPermissionService;

}