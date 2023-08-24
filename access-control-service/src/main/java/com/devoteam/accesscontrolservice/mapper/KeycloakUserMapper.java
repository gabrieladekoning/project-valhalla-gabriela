package com.devoteam.accesscontrolservice.mapper;

import com.devoteam.accesscontrolservice.entity.ApplicationUser;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface KeycloakUserMapper {

  @Mapping(source = "email", target = "username")
  UserRepresentation mapToUserRepresentation(ApplicationUser applicationUser);
}