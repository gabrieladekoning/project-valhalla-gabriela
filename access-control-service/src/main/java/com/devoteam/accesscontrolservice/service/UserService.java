package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.ApplicationUserDto;

public interface UserService {
    ApplicationUserDto saveApplicationUser(ApplicationUserDto userDto);
}
