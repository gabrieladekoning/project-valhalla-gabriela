package com.devoteam.accesscontrolservice.mapper;

import com.devoteam.accesscontrolservice.dto.ApplicationUserDto;
import com.devoteam.accesscontrolservice.entity.ApplicationUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ApplicationUserMapper {
    ApplicationUserMapper INSTANCE = Mappers.getMapper(ApplicationUserMapper.class);

    ApplicationUser mapToApplicationUser (ApplicationUserDto dto);
    ApplicationUserDto mapToApplicationUserDto (ApplicationUser user);
}
