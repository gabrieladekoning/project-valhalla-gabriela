package com.devoteam.securitycore.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CheckPermissionService {
    private final WebClient webClient;

    public CheckPermissionService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }


    public boolean isAccessAllowed(String applicationName, String functionName, String permissionName) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String token = jwt.getTokenValue();

        log.info("Checking access based on application name: '{}', function name: '{}', and permission name: '{}'",
                applicationName, functionName, permissionName);

        try {
            return Boolean.TRUE.equals(webClient.get()
                    .uri("http://access-control-service/v1/access-validation/validate", uriBuilder -> uriBuilder
                            .queryParam("applicationName", applicationName)
                            .queryParam("functionName", functionName)
                            .queryParam("permission", permissionName)
                            .build())
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new AccessDeniedException("Access Denied")))
                    .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Server Error")))
                    .toEntity(Void.class)
                    .map(responseEntity -> responseEntity.getStatusCode() == HttpStatus.OK)
                    .block());
        } catch (AccessDeniedException exception) {
            log.error("Access denied to application: '{}', function: '{}', with permission: '{}'", applicationName, functionName, permissionName);
            return false;
        } catch (Exception exception) {
            log.error("Error occurred while validating access", exception);
            return false;
        }
    }
}