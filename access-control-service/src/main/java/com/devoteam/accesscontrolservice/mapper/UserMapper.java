package com.devoteam.accesscontrolservice.mapper;

import com.devoteam.accesscontrolservice.dto.ApplicationUserCreationRequest;
import com.devoteam.accesscontrolservice.dto.ApplicationUserCreationResponse;
import com.devoteam.accesscontrolservice.entity.ApplicationUser;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    ApplicationUser mapToApplicationUser(ApplicationUserCreationRequest dto);

    ApplicationUserCreationResponse mapToApplicationUserDto(ApplicationUser user);
}