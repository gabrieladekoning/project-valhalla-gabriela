package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccessValidationService {
    private final UserRepository userRepository;

    public boolean checkAccess(String applicationName, String functionName, String permissionName) {
        var userUuid = extractUserUuid();
        return userRepository.checkUserAccess(userUuid, applicationName, functionName, permissionName);
    }

    public UUID extractUserUuid() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = jwt.getClaim("sub");
        return UUID.fromString(userId);
    }
}