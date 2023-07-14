package com.devoteam.accesscontrolservice.mapper;

import com.devoteam.accesscontrolservice.dto.BusinessFunctionPermissionCreationRequest;
import com.devoteam.accesscontrolservice.dto.BusinessFunctionPermissionCreationResponse;
import com.devoteam.accesscontrolservice.entity.BusinessFunctionPermission;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BusinessFunctionPermissionMapper {
    BusinessFunctionPermissionMapper INSTANCE = Mappers.getMapper(BusinessFunctionPermissionMapper.class);

    BusinessFunctionPermission mapToBusinessFunctionPermission(BusinessFunctionPermissionCreationRequest creationRequest);

    BusinessFunctionPermissionCreationResponse mapToBusinessFunctionPermissionResponse(BusinessFunctionPermission businessFunctionPermission);
}
