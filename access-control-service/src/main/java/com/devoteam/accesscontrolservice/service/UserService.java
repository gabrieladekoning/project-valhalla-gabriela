package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.ApplicationUserCreationRequest;
import com.devoteam.accesscontrolservice.dto.ApplicationUserCreationResponse;
import com.devoteam.accesscontrolservice.exception.EmailAlreadyExistsException;
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

    public ApplicationUserCreationResponse saveApplicationUser(ApplicationUserCreationRequest creationRequest) {
        assertEmailDoesNotExist(creationRequest);
        var user = UserMapper.INSTANCE.mapToApplicationUser(creationRequest);
        log.info("Creating user");
        var keycloakUserId = keycloakService.createUser(creationRequest);
        user.setId(UUID.fromString(keycloakUserId));
        var savedUser = userRepository.save(user);
        log.debug("Saved user with ID: {}", savedUser.getId());
        return UserMapper.INSTANCE.mapToApplicationUserDto(savedUser);
    }

    private void assertEmailDoesNotExist(ApplicationUserCreationRequest creationRequest) {
        userRepository.findByEmail(creationRequest.getEmail()).ifPresent(user -> {
            throw new EmailAlreadyExistsException("Email is already registered: '%s'".formatted(user.getEmail()));
        });
    }
}