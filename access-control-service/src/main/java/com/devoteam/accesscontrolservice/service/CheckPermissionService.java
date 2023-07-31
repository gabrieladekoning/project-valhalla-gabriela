package com.devoteam.accesscontrolservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckPermissionService {
    private final AccessValidationService accessValidationService;

    public boolean isAccessAllowed(String applicationName, String functionName, String permissionName) {
        log.info("Checking access based on application name: '{}', function name: '{}', and permission name: '{}'", applicationName, functionName, permissionName);
        return accessValidationService.checkAccess(applicationName, functionName, permissionName);
    }
}
