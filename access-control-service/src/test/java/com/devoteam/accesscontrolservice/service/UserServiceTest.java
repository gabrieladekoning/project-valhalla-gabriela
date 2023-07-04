package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.ApplicationUserRequest;
import com.devoteam.accesscontrolservice.dto.ApplicationUserCreationResponse;
import com.devoteam.accesscontrolservice.entity.ApplicationUser;
import com.devoteam.accesscontrolservice.exception.EmailAlreadyExistsException;
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
    @InjectMocks
    private UserService userService;
    private ApplicationUserRequest testUserDto;
    private ApplicationUser testUser;

    @BeforeEach
    public void setup() {
        UUID uuid = UUID.randomUUID();
        testUserDto = ApplicationUserRequest.builder()
                .firstName("Tofu")
                .lastName("Taro")
                .email("tofu_taro@test.com")
                .password("verystrongpassword")
                .build();

        testUser = ApplicationUser.builder()
                .id(uuid)
                .firstName(testUserDto.getFirstName())
                .lastName(testUserDto.getLastName())
                .email(testUserDto.getEmail())
                .build();
    }

    @DisplayName("Test saving a new user successfully")
    @Test
    void saveApplicationUser_SuccessfullySaved_WhenUserDoesNotAlreadyExist() {
        given(userRepository.findByEmail(testUserDto.getEmail())).willReturn(Optional.empty());
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
}