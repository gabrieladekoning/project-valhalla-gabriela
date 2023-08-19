package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccessValidationServiceTest {

  @Mock
  private UserRepository userRepository;
  @InjectMocks
  private AccessValidationService accessValidationService;
  private String applicationName;
  private String functionName;
  private String permissionName;
  private UUID userUuid;

  @BeforeEach
  void setup() {
    userUuid = UUID.randomUUID();

    applicationName = "Application name";
    functionName = "Function name";
    permissionName = "Permission name";

    Jwt jwt = Jwt.withTokenValue("token")
        .header("alg", "none")
        .claim("sub", userUuid.toString())
        .build();

    Authentication authentication = Mockito.mock(Authentication.class);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);

    when(authentication.getPrincipal()).thenReturn(jwt);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
  }

  @DisplayName("Access validation when user has access")
  @Test
  void testAccessValidation_Success_WhenUserHasAccess() {
    given(userRepository.checkUserAccess(userUuid, applicationName, functionName, permissionName)).willReturn(1L);

    boolean accessResult = accessValidationService.checkAccess(applicationName, functionName, permissionName);

    assertTrue(accessResult);
  }

  @DisplayName("Access validation when user does not have access")
  @Test
  void testAccessValidation_Fail_WhenUserDoesNotHaveAccess() {
    given(userRepository.checkUserAccess(userUuid, applicationName, functionName, permissionName)).willReturn(0L);

    boolean accessResult = accessValidationService.checkAccess(applicationName, functionName, permissionName);

    assertFalse(accessResult);
  }
}