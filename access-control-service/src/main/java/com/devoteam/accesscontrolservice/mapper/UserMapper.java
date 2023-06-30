package com.devoteam.accesscontrolservice.mapper;

import com.devoteam.accesscontrolservice.dto.ApplicationUserDto;
import com.devoteam.accesscontrolservice.entity.ApplicationUser;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    ApplicationUser mapToApplicationUser(ApplicationUserDto dto);

    ApplicationUserDto mapToApplicationUserDto(ApplicationUser user);
}
