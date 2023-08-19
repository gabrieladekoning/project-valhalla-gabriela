package com.devoteam.accesscontrolservice.mapper;

import com.devoteam.accesscontrolservice.dto.ProfileBusinessFunctionPermissionCreationRequest;
import com.devoteam.accesscontrolservice.dto.ProfileBusinessFunctionPermissionCreationResponse;
import com.devoteam.accesscontrolservice.dto.ProfileBusinessFunctionPermissionReadResponse;
import com.devoteam.accesscontrolservice.entity.ProfileBusinessFunctionPermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING, uses = {BusinessFunctionPermissionMapper.class, ProfileMapper.class})

public interface ProfileBusinessFunctionPermissionMapper {

  @Mapping(source = "businessFunctionPermissionId", target = "businessFunctionPermission.id")
  @Mapping(source = "profileId", target = "profile.id")
  ProfileBusinessFunctionPermission mapToProfileBusinessFunctionPermission(
      ProfileBusinessFunctionPermissionCreationRequest creationRequest);

  ProfileBusinessFunctionPermissionCreationResponse mapToProfileBusinessFunctionPermissionCreationResponse(
      ProfileBusinessFunctionPermission profileBusinessFunctionPermission);

  @Mapping(source = "profile", target = "profile")
  @Mapping(source = "businessFunctionPermission", target = "businessFunctionPermission")
  ProfileBusinessFunctionPermissionReadResponse mapToProfileBusinessFunctionPermissionReadResponse(
      ProfileBusinessFunctionPermission profileBusinessFunctionPermission);
}