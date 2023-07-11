package com.devoteam.accesscontrolservice.mapper;

import com.devoteam.accesscontrolservice.dto.ApplicationUserCreationRequest;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface KeycloakUserMapper {
    @Mapping(source = "email", target = "username")
    UserRepresentation mapToUserRepresentation(ApplicationUserCreationRequest dto);
}