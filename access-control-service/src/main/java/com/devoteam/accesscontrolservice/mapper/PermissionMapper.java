package com.devoteam.accesscontrolservice.mapper;

import com.devoteam.accesscontrolservice.dto.PermissionCreationRequest;
import com.devoteam.accesscontrolservice.dto.PermissionCreationResponse;
import com.devoteam.accesscontrolservice.dto.PermissionReadResponse;
import com.devoteam.accesscontrolservice.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface PermissionMapper {
    Permission mapToPermission(PermissionCreationRequest creationRequest);

    PermissionCreationResponse mapToPermissionCreationResponse(Permission permission);

    PermissionReadResponse mapToPermissionReadResponse(Permission permission);
}