package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.ApplicationUserCreationRequest;
import com.devoteam.accesscontrolservice.dto.ApplicationUserCreationResponse;
import com.devoteam.accesscontrolservice.entity.ApplicationUser;
import com.devoteam.accesscontrolservice.exception.EmailAlreadyExistsException;
import com.devoteam.accesscontrolservice.exception.UserCreationException;
import com.devoteam.accesscontrolservice.mapper.UserMapperImpl;
import com.devoteam.accesscontrolservice.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@Import(UserMapperImpl.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private KeycloakService keycloakService;
    @InjectMocks
    private UserService userService;
    private ApplicationUserCreationRequest testUserDto;
    private ApplicationUser testUser;
    private String uuid;

    @BeforeEach
    public void setup() {
        uuid = UUID.randomUUID().toString();
        testUserDto = ApplicationUserCreationRequest.builder()
                .firstName("Tofu")
                .lastName("Taro")
                .email("tofu_taro@test.com")
                .password("verystrongpassword")
                .build();

        testUser = ApplicationUser.builder()
                .id(UUID.fromString(uuid))
                .firstName(testUserDto.getFirstName())
                .lastName(testUserDto.getLastName())
                .email(testUserDto.getEmail())
                .build();
    }

    @DisplayName("Test saving a new user successfully")
    @Test
    void saveApplicationUser_SuccessfullySaved_WhenUserDoesNotAlreadyExist() {
        given(userRepository.findByEmail(testUserDto.getEmail())).willReturn(Optional.empty());
        given(keycloakService.createUser(testUserDto)).willReturn(uuid);
        given(userRepository.save(ArgumentMatchers.any())).willReturn(testUser);

        ApplicationUserCreationResponse savedUserDto = userService.saveApplicationUser(testUserDto);
        savedUserDto.setId(testUser.getId());

        Assertions.assertThat(savedUserDto).hasNoNullFieldsOrProperties();
    }

    @DisplayName("Test saving a user with existing email")
    @Test
    void saveApplicationUser_ThrowsEmailAlreadyExistsException_WhenEmailAlreadyExists() {
        given(userRepository.findByEmail(testUserDto.getEmail())).willReturn(Optional.of(testUser));

        assertThrows(EmailAlreadyExistsException.class, () -> userService.saveApplicationUser(testUserDto));
        then(userRepository).should(times(1)).findByEmail(testUserDto.getEmail());
        then(userRepository).should(times(0)).save(any(ApplicationUser.class));
    }

    @DisplayName("Test saving a user when Keycloak registration fails")
    @Test
    void saveApplicationUser_ThrowsUserCreationException_WhenKeycloakRegistrationFails() {
        given(userRepository.findByEmail(testUserDto.getEmail())).willReturn(Optional.empty());
        given(keycloakService.createUser(testUserDto)).willThrow(new UserCreationException("Failed to create user in Keycloak"));

        assertThrows(UserCreationException.class, () -> userService.saveApplicationUser(testUserDto));
        then(userRepository).should(times(1)).findByEmail(testUserDto.getEmail());
        then(userRepository).should(times(0)).save(any(ApplicationUser.class));
    }
}