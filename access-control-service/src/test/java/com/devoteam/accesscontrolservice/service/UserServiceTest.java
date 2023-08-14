package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.ApplicationUserCreationRequest;
import com.devoteam.accesscontrolservice.entity.ApplicationUser;
import com.devoteam.accesscontrolservice.exception.EmailAlreadyExistsException;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.exception.UserCreationFailedException;
import com.devoteam.accesscontrolservice.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private KeycloakService keycloakService;
    @InjectMocks
    private UserService userService;
    private ApplicationUserCreationRequest userCreationRequest;
    private ApplicationUser user;
    private UUID uuid;

    @BeforeEach
    public void setup() {
        uuid = UUID.randomUUID();
        userCreationRequest = ApplicationUserCreationRequest.builder()
                .firstName("Tofu")
                .lastName("Taro")
                .email("tofu_taro@test.com")
                .password("verystrongpassword")
                .build();

        user = ApplicationUser.builder()
                .id(uuid)
                .firstName(userCreationRequest.getFirstName())
                .lastName(userCreationRequest.getLastName())
                .email(userCreationRequest.getEmail())
                .build();
    }

    @DisplayName("Saving a new user successfully")
    @Test
    void saveApplicationUser_SuccessfullySaved_WhenUserDoesNotAlreadyExist() {
        given(userRepository.findByEmail(userCreationRequest.getEmail())).willReturn(Optional.empty());
        given(keycloakService.createUser(userCreationRequest)).willReturn(String.valueOf(uuid));
        given(userRepository.save(any())).willReturn(user);

        var savedUser = userService.saveApplicationUser(userCreationRequest);
        savedUser.setId(user.getId());

        Assertions.assertThat(savedUser).hasNoNullFieldsOrProperties();
    }

    @DisplayName("Saving a user with existing email")
    @Test
    void saveApplicationUser_ThrowsEmailAlreadyExistsException_WhenEmailAlreadyExists() {
        given(userRepository.findByEmail(userCreationRequest.getEmail())).willReturn(Optional.of(user));

        assertThrows(EmailAlreadyExistsException.class, () -> userService.saveApplicationUser(userCreationRequest));
        then(userRepository).should(times(1)).findByEmail(userCreationRequest.getEmail());
        then(userRepository).should(times(0)).save(any(ApplicationUser.class));
    }

    @DisplayName("Saving a user when Keycloak registration fails")
    @Test
    void saveApplicationUser_ThrowsUserCreationException_WhenKeycloakRegistrationFails() {
        given(userRepository.findByEmail(userCreationRequest.getEmail())).willReturn(Optional.empty());
        given(keycloakService.createUser(userCreationRequest)).willThrow(new UserCreationFailedException("Failed to create user in Keycloak"));

        assertThrows(UserCreationFailedException.class, () -> userService.saveApplicationUser(userCreationRequest));
        then(userRepository).should(times(1)).findByEmail(userCreationRequest.getEmail());
        then(userRepository).should(times(0)).save(any(ApplicationUser.class));
    }

    @DisplayName("Handling when user is not found by ID")
    @Test
    void findByIdOrThrowNotFound_ThrowsResourceNotFoundException_WhenUserNotFound() {
        given(userRepository.findById(user.getId())).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findByIdOrThrowNotFound(String.valueOf(user.getId())));
    }

    @DisplayName("Handling when user is not found by email")
    @Test
    void  findByEmailOrThrowNotFound_ThrowsResourceNotFoundException_WhenUserNotFound() {
        given(userRepository.findByEmail(userCreationRequest.getEmail())).willReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findByEmailOrThrowNotFound(userCreationRequest.getEmail()));
    }
}