package com.devoteam.accesscontrolservice.mapper;

import com.devoteam.accesscontrolservice.dto.ApplicationUserRequestDto;
import com.devoteam.accesscontrolservice.dto.ApplicationUserResponseDto;
import com.devoteam.accesscontrolservice.entity.ApplicationUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "Spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    ApplicationUser mapToApplicationUser(ApplicationUserRequestDto dto);

    ApplicationUserResponseDto mapToApplicationUserDto(ApplicationUser user);
}
