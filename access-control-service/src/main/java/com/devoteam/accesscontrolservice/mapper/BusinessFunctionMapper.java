package com.devoteam.accesscontrolservice.mapper;

import com.devoteam.accesscontrolservice.dto.BusinessFunctionCreationRequest;
import com.devoteam.accesscontrolservice.dto.BusinessFunctionCreationResponse;
import com.devoteam.accesscontrolservice.entity.BusinessFunction;
import org.mapstruct.Mapper;

@Mapper
public interface BusinessFunctionMapper {

    BusinessFunction mapToBusinessFunction(BusinessFunctionCreationRequest request);

    BusinessFunctionCreationResponse mapToBusinessFunctionResponse(BusinessFunction businessFunction);
}
