package com.devoteam.accesscontrolservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckPermissionServiceTest {

    @Mock
    private AccessValidationService accessValidationService;

    @InjectMocks
    private CheckPermissionService checkPermissionService;

    private String applicationName;
    private String functionName;
    private String permissionName;

    @BeforeEach
    void setup() {
        applicationName = "access-control-service";
        functionName = "business-function";
        permissionName = "create";
    }

    @DisplayName("Test access check when user has access")
    @Test
    void testIsAccessAllowed_whenUserHasAccess() {
        when(accessValidationService.checkAccess(applicationName, functionName, permissionName))
                .thenReturn(true);

        boolean result = checkPermissionService.isAccessAllowed(applicationName, functionName, permissionName);

        assertTrue(result);
    }

    @DisplayName("Test access check when user does not have access")
    @Test
    void testIsAccessAllowed_whenUserDoesNotHaveAccess() {
        when(accessValidationService.checkAccess(applicationName, functionName, permissionName))
                .thenReturn(false);

        boolean result = checkPermissionService.isAccessAllowed(applicationName, functionName, permissionName);

        assertFalse(result);
    }
}