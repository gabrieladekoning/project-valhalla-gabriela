package com.devoteam.accesscontrolservice.mapper;

import com.devoteam.accesscontrolservice.dto.BusinessFunctionPermissionCreationRequest;
import com.devoteam.accesscontrolservice.dto.BusinessFunctionPermissionCreationResponse;
import com.devoteam.accesscontrolservice.entity.BusinessFunctionPermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")

public interface BusinessFunctionPermissionMapper {
    BusinessFunctionPermissionMapper INSTANCE = Mappers.getMapper(BusinessFunctionPermissionMapper.class);

    @Mapping(source = "businessFunctionId", target = "businessFunction.id")
    @Mapping(source = "permissionId", target = "permission.id")
    BusinessFunctionPermission mapToBusinessFunctionPermission(BusinessFunctionPermissionCreationRequest creationRequest);

    BusinessFunctionPermissionCreationResponse mapToBusinessFunctionPermissionResponse(BusinessFunctionPermission businessFunctionPermission);
}
