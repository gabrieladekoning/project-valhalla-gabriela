package com.devoteam.accesscontrolservice.mapper;

import com.devoteam.accesscontrolservice.dto.ApplicationUserCreationRequest;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface KeycloakUserMapper {
    KeycloakUserMapper INSTANCE = Mappers.getMapper(KeycloakUserMapper.class);

    @Mapping(source = "email", target = "username")
    UserRepresentation mapToUserRepresentation(ApplicationUserCreationRequest creationRequest);
}