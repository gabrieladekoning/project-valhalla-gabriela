package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.ApplicationUserDto;
import com.devoteam.accesscontrolservice.entity.ApplicationUser;
import com.devoteam.accesscontrolservice.mapper.ApplicationUserMapper;
import com.devoteam.accesscontrolservice.repository.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationUserService {
    private final ApplicationUserRepository applicationUserRepository;
    private final ApplicationUserMapper applicationUserMapper;
    public ApplicationUserDto saveApplicationUser (ApplicationUserDto applicationUserDto) {
        ApplicationUser user = applicationUserMapper.mapToApplicationUser(applicationUserDto);
        UUID id = UUID.randomUUID();
        user.setId(id);
        ApplicationUser savedUser = applicationUserRepository.save(user);
        return applicationUserMapper.mapToApplicationUserDto(savedUser);
    }
}
