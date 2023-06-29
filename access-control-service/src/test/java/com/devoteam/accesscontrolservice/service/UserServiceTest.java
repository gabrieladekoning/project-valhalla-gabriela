package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.ApplicationUserDto;
import com.devoteam.accesscontrolservice.entity.ApplicationUser;
import com.devoteam.accesscontrolservice.exception.EmailAlreadyExistsException;
import com.devoteam.accesscontrolservice.mapper.UserMapper;
import com.devoteam.accesscontrolservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private UserMapper userMapper;
    @InjectMocks
    private UserService userService;
    private ApplicationUserDto testUserDto;
    private ApplicationUser testUser;

    @BeforeEach
    public void setup() {
        UUID uuid = UUID.randomUUID();
        testUserDto = new ApplicationUserDto("Tofu", "Taro", "tofu_taro@test.com");
        testUser = new ApplicationUser(uuid, testUserDto.getFirstName(), testUserDto.getLastName(), testUserDto.getEmail());

    }

    @DisplayName("Test saving a new user successfully")
    @Test
    void saveApplicationUser_SuccessfullySaved_WhenUserDoesNotAlreadyExist() {
        given(userRepository.findByEmail(testUserDto.getEmail())).willReturn(Optional.empty());
        given(userMapper.mapToUser(testUserDto)).willReturn(testUser);
        given(userRepository.save(testUser)).willReturn(testUser);
        given(userMapper.mapToUserDto(testUser)).willReturn(testUserDto);

        ApplicationUserDto savedUserDto = userService.saveApplicationUser(testUserDto);

        assertEquals(testUserDto, savedUserDto);
        then(userRepository).should(times(1)).findByEmail(testUserDto.getEmail());
        then(userRepository).should(times(1)).save(testUser);
    }

    @DisplayName("Test saving a user with existing email")
    @Test
    void saveApplicationUser_ThrowsEmailAlreadyExistsException_WhenEmailAlreadyExists() {
        given(userRepository.findByEmail(testUserDto.getEmail())).willThrow(EmailAlreadyExistsException.class);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.saveApplicationUser(testUserDto));
        then(userRepository).should(times(1)).findByEmail(testUserDto.getEmail());
        then(userRepository).should(times(0)).save(any(ApplicationUser.class));
    }
}