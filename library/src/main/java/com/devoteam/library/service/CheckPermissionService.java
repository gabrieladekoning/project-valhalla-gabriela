package com.devoteam.library.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class CheckPermissionService {
    private final WebClient webClient;

    public CheckPermissionService(@Value("${access.control.service.url}") String accessControlServiceUrl) {
        this.webClient = WebClient.builder().baseUrl(accessControlServiceUrl).build();
    }


    public boolean isAccessAllowed(String applicationName, String functionName, String permissionName) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String token = jwt.getTokenValue();

        log.info("Checking access based on application name: '{}', function name: '{}', and permission name: '{}'",
                applicationName, functionName, permissionName);

        ResponseEntity<Void> response = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/v1/access-validation/validate")
                        .queryParam("applicationName", applicationName)
                        .queryParam("functionName", functionName)
                        .queryParam("permission", permissionName)
                        .build())
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .toEntity(Void.class)
                .block();

        return response != null && response.getStatusCode() == HttpStatus.OK;
    }
}