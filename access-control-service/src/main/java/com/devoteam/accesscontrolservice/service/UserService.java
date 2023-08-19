package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.ApplicationUserCreationRequest;
import com.devoteam.accesscontrolservice.entity.ApplicationUser;
import com.devoteam.accesscontrolservice.exception.EmailAlreadyExistsException;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.mapper.UserMapper;
import com.devoteam.accesscontrolservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final KeycloakService keycloakService;
    private final UserMapper userMapper;

    public ApplicationUser saveApplicationUser(ApplicationUserCreationRequest creationRequest) {
        assertEmailDoesNotExist(creationRequest);
        var user = userMapper.mapToApplicationUser(creationRequest);
        log.info("Creating user with email: {}", creationRequest.getEmail());

        var keycloakUserId = keycloakService.createUser(creationRequest);
        user.setId(UUID.fromString(keycloakUserId));
        return userRepository.save(user);
    }

    private void assertEmailDoesNotExist(ApplicationUserCreationRequest creationRequest) {
        log.info("Checking if email exists");
        userRepository.findByEmail(creationRequest.getEmail()).ifPresent(user -> {
            throw new EmailAlreadyExistsException("Email is already registered: '%s'".formatted(user.getEmail()));
        });
    }

    public ApplicationUser findByIdOrThrowNotFound(String userId) {
        log.info("Finding user by ID");
        return userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with ID: %s not found.".formatted(userId)
                ));
    }

    public ApplicationUser findByEmailOrThrowNotFound(String email) {
        log.info("Finding user by email");
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with email: %s not found.".formatted(email)
                ));
    }
}