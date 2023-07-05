package com.devoteam.accesscontrolservice.mapper;

import com.devoteam.accesscontrolservice.dto.ApplicationUserCreationRequest;
import com.devoteam.accesscontrolservice.dto.ApplicationUserCreationResponse;
import com.devoteam.accesscontrolservice.entity.ApplicationUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "Spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    ApplicationUser mapToApplicationUser(ApplicationUserCreationRequest dto);

    ApplicationUserCreationResponse mapToApplicationUserDto(ApplicationUser user);
}
