package com.devoteam.accesscontrolservice.mapper;

import com.devoteam.accesscontrolservice.dto.ProfileBusinessFunctionPermissionCreationRequest;
import com.devoteam.accesscontrolservice.dto.ProfileBusinessFunctionPermissionCreationResponse;
import com.devoteam.accesscontrolservice.dto.ProfileBusinessFunctionPermissionReadResponse;
import com.devoteam.accesscontrolservice.entity.ProfileBusinessFunctionPermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)

public interface ProfileBusinessFunctionPermissionMapper {

  @Mapping(source = "businessFunctionPermissionId", target = "businessFunctionPermission.id")
  @Mapping(source = "profileId", target = "profile.id")
  ProfileBusinessFunctionPermission mapToProfileBusinessFunctionPermission(
      ProfileBusinessFunctionPermissionCreationRequest creationRequest);

  ProfileBusinessFunctionPermissionCreationResponse mapToProfileBusinessFunctionPermissionCreationResponse(
      ProfileBusinessFunctionPermission profileBusinessFunctionPermission);

  @Mapping(source = "businessFunctionPermission.id", target = "businessFunctionPermission.id")
  @Mapping(source = "businessFunctionPermission.businessFunction.id", target = "businessFunctionPermission.businessFunction.id")
  @Mapping(source = "businessFunctionPermission.businessFunction.applicationName", target = "businessFunctionPermission.businessFunction.applicationName")
  @Mapping(source = "businessFunctionPermission.businessFunction.functionName", target = "businessFunctionPermission.businessFunction.functionName")
  @Mapping(source = "businessFunctionPermission.permission.id", target = "businessFunctionPermission.permission.id")
  @Mapping(source = "businessFunctionPermission.permission.name", target = "businessFunctionPermission.permission.name")
  @Mapping(source = "profile.id", target = "profile.id")
  @Mapping(source = "profile.name", target = "profile.name")
  ProfileBusinessFunctionPermissionReadResponse mapToProfileBusinessFunctionPermissionReadResponse(
      ProfileBusinessFunctionPermission profileBusinessFunctionPermission);

}