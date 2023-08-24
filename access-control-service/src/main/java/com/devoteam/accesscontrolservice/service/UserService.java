package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.entity.ApplicationUser;
import com.devoteam.accesscontrolservice.exception.EmailAlreadyExistsException;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

  private final UserRepository userRepository;
  private final KeycloakService keycloakService;

  public ApplicationUser saveApplicationUser(ApplicationUser applicationUser) {
    assertEmailDoesNotExist(applicationUser.getEmail());

    log.info("Creating user with email: '{}'", applicationUser.getEmail());

    var keycloakUserId = keycloakService.createUser(applicationUser);
    log.debug("Setting ID generated from keycloak");
    applicationUser.setId(UUID.fromString(keycloakUserId));

    log.info("Saving user with ID: '{}' and email: '{}'", applicationUser.getId(), applicationUser.getEmail());
    return userRepository.save(applicationUser);
  }

  private void assertEmailDoesNotExist(String email) {
    log.info("Checking if email exists");
    userRepository.findByEmail(email).ifPresent(user -> {
      throw new EmailAlreadyExistsException("Email is already registered: '%s'".formatted(user.getEmail()));
    });
  }

  public ApplicationUser findByIdOrThrowNotFound(UUID userId) {
    log.info("Finding user by ID");
    return userRepository.findById(userId)
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

  public Page<ApplicationUser> searchAndListUsers(String firstName,
      String lastName, String email, Pageable pageable) {
    log.info("Searching for users");

    return userRepository.findByFirstNameContainingOrLastNameContainingOrEmailContaining(firstName, lastName, email,
        pageable);
  }
}