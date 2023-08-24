package com.devoteam.accesscontrolservice.mapper;

import com.devoteam.accesscontrolservice.dto.BusinessFunctionPermissionCreationRequest;
import com.devoteam.accesscontrolservice.dto.BusinessFunctionPermissionCreationResponse;
import com.devoteam.accesscontrolservice.dto.BusinessFunctionPermissionReadResponse;
import com.devoteam.accesscontrolservice.entity.BusinessFunctionPermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface BusinessFunctionPermissionMapper {

  @Mapping(source = "businessFunctionId", target = "businessFunction.id")
  @Mapping(source = "permissionId", target = "permission.id")
  BusinessFunctionPermission mapToBusinessFunctionPermission(BusinessFunctionPermissionCreationRequest creationRequest);

  BusinessFunctionPermissionCreationResponse mapToBusinessFunctionPermissionCreationResponse(
      BusinessFunctionPermission businessFunctionPermission);

  @Mapping(source = "businessFunction.id", target = "businessFunction.id")
  @Mapping(source = "businessFunction.applicationName", target = "businessFunction.applicationName")
  @Mapping(source = "businessFunction.functionName", target = "businessFunction.functionName")
  @Mapping(source = "permission.id", target = "permission.id")
  @Mapping(source = "permission.name", target = "permission.name")
  BusinessFunctionPermissionReadResponse mapToBusinessFunctionPermissionReadResponse(
      BusinessFunctionPermission businessFunctionPermission);
}