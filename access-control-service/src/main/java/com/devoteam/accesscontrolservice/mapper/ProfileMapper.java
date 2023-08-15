package com.devoteam.accesscontrolservice.mapper;

import com.devoteam.accesscontrolservice.dto.ProfileCreationRequest;
import com.devoteam.accesscontrolservice.dto.ProfileCreationResponse;
import com.devoteam.accesscontrolservice.dto.ProfileReadResponse;
import com.devoteam.accesscontrolservice.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);

    Profile mapToProfile(ProfileCreationRequest creationRequest);

    ProfileCreationResponse mapToProfileCreationResponse(Profile permission);

    ProfileReadResponse mapToProfileReadResponse(Profile permission);
}