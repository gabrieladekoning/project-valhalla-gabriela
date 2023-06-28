package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.ApplicationUserDto;
import com.devoteam.accesscontrolservice.entity.ApplicationUser;
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
    private final UserMapper userMapper;
    public ApplicationUserDto saveApplicationUser (ApplicationUserDto applicationUserDto) {
        ApplicationUser user = userMapper.mapToUser(applicationUserDto);
        UUID id = UUID.randomUUID();
        user.setId(id);
        ApplicationUser savedUser = userRepository.save(user);
        log.info("saving user (service)");
        return userMapper.mapToUserDto(savedUser);
    }
}
