package com.devoteam.accesscontrolservice.mapper;

import com.devoteam.accesscontrolservice.dto.BusinessFunctionPermissionCreationRequest;
import com.devoteam.accesscontrolservice.dto.BusinessFunctionPermissionCreationResponse;
import com.devoteam.accesscontrolservice.dto.BusinessFunctionPermissionReadResponse;
import com.devoteam.accesscontrolservice.entity.BusinessFunctionPermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING, uses = {BusinessFunctionMapper.class, PermissionMapper.class})
public interface BusinessFunctionPermissionMapper {

    @Mapping(source = "businessFunctionId", target = "businessFunction.id")
    @Mapping(source = "permissionId", target = "permission.id")
    BusinessFunctionPermission mapToBusinessFunctionPermission(BusinessFunctionPermissionCreationRequest creationRequest);

    BusinessFunctionPermissionCreationResponse mapToBusinessFunctionPermissionCreationResponse(BusinessFunctionPermission businessFunctionPermission);

    @Mapping(source = "businessFunction", target = "businessFunction")
    @Mapping(source = "permission", target = "permission")
    BusinessFunctionPermissionReadResponse mapToBusinessFunctionPermissionReadResponse(BusinessFunctionPermission businessFunctionPermission);
}