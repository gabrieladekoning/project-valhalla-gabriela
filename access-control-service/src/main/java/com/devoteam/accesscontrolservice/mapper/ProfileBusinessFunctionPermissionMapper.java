package com.devoteam.accesscontrolservice.mapper;

import com.devoteam.accesscontrolservice.dto.ProfileBusinessFunctionPermissionCreationRequest;
import com.devoteam.accesscontrolservice.dto.ProfileBusinessFunctionPermissionCreationResponse;
import com.devoteam.accesscontrolservice.entity.ProfileBusinessFunctionPermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")

public interface ProfileBusinessFunctionPermissionMapper {
    ProfileBusinessFunctionPermissionMapper INSTANCE = Mappers.getMapper(ProfileBusinessFunctionPermissionMapper.class);

    @Mapping(source = "businessFunctionPermissionId", target = "businessFunctionPermission.id")
    @Mapping(source = "profileId", target = "profile.id")
    ProfileBusinessFunctionPermission mapToProfileBusinessFunctionPermission(ProfileBusinessFunctionPermissionCreationRequest creationRequest);

    ProfileBusinessFunctionPermissionCreationResponse mapToProfileBusinessFunctionPermissionResponse(ProfileBusinessFunctionPermission profileBusinessFunctionPermission);
}