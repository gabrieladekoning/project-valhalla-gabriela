package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.ApplicationUserRequestDto;
import com.devoteam.accesscontrolservice.dto.ApplicationUserResponseDto;
import com.devoteam.accesscontrolservice.entity.ApplicationUser;
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
    private final UserMapper userMapper = UserMapper.INSTANCE;

    public ApplicationUserResponseDto saveApplicationUser(ApplicationUserRequestDto userDto) {
        findByEmailOrThrowException(userDto);
        ApplicationUser user = userMapper.mapToApplicationUser(userDto);
        UUID id = UUID.randomUUID();
        user.setId(id);
        ApplicationUser savedUser = userRepository.save(user);
        log.debug("Saved user with ID: {}", savedUser.getId());
        return userMapper.mapToApplicationUserDto(savedUser);
    }

    private void findByEmailOrThrowException(ApplicationUserRequestDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email is already registered: " + userDto.getEmail());
        }
    }
}