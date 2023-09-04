package com.devoteam.accesscontrolservice.mapper;

import com.devoteam.accesscontrolservice.dto.BusinessFunctionCreationRequest;
import com.devoteam.accesscontrolservice.dto.BusinessFunctionCreationResponse;
import com.devoteam.accesscontrolservice.dto.BusinessFunctionReadResponse;
import com.devoteam.accesscontrolservice.dto.BusinessFunctionUpdateRequest;
import com.devoteam.accesscontrolservice.entity.BusinessFunction;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface BusinessFunctionMapper {

  BusinessFunction mapToBusinessFunction(BusinessFunctionCreationRequest creationRequest);

  BusinessFunction mapToBusinessFunction(BusinessFunctionUpdateRequest updateRequest);

  BusinessFunctionCreationResponse mapToBusinessFunctionCreationResponse(BusinessFunction businessFunction);

  BusinessFunctionReadResponse mapToBusinessFunctionReadResponse(BusinessFunction businessFunction);
}