package com.devoteam.accesscontrolservice.mapper;

import com.devoteam.accesscontrolservice.dto.ApplicationUserCreationRequest;
import com.devoteam.accesscontrolservice.dto.ApplicationUserCreationResponse;
import com.devoteam.accesscontrolservice.entity.ApplicationUser;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface UserMapper {
    ApplicationUser mapToApplicationUser(ApplicationUserCreationRequest creationRequest);

    ApplicationUserCreationResponse mapToApplicationUserCreationResponse(ApplicationUser user);
}