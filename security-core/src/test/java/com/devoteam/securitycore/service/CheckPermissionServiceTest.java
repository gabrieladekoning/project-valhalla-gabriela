package com.devoteam.securitycore.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckPermissionServiceTest {
    @InjectMocks
    private CheckPermissionService checkPermissionService;
    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private String applicationName;
    private String functionName;
    private String permissionName;

    @BeforeEach
    void setUp() {
        UUID userUuid = UUID.randomUUID();

        applicationName = "Application name";
        functionName = "Function name";
        permissionName = "Permission name";

        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", userUuid.toString())
                .build();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(jwt);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.toEntity(Void.class)).thenReturn(Mono.just(ResponseEntity.ok().build()));  // Simulates a successful response

        checkPermissionService = new CheckPermissionService(webClientBuilder);
    }

    @DisplayName("Test if access is allowed when user has permission")
    @Test
    void shouldAllowAccess_WhenUserHasPermission() {
        boolean result = checkPermissionService.isAccessAllowed(applicationName, functionName, permissionName);

        assertTrue(result);
    }
}

