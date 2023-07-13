package com.devoteam.accesscontrolservice.mapper;

import com.devoteam.accesscontrolservice.dto.BusinessFunctionCreationRequest;
import com.devoteam.accesscontrolservice.dto.BusinessFunctionCreationResponse;
import com.devoteam.accesscontrolservice.entity.BusinessFunction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BusinessFunctionMapper {
    BusinessFunctionMapper INSTANCE = Mappers.getMapper(BusinessFunctionMapper.class);
    BusinessFunction mapToBusinessFunction(BusinessFunctionCreationRequest request);

    BusinessFunctionCreationResponse mapToBusinessFunctionResponse(BusinessFunction businessFunction);
}
