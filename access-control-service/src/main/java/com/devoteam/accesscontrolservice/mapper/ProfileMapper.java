package com.devoteam.accesscontrolservice.mapper;

import com.devoteam.accesscontrolservice.dto.ProfileCreationRequest;
import com.devoteam.accesscontrolservice.dto.ProfileCreationResponse;
import com.devoteam.accesscontrolservice.dto.ProfileReadResponse;
import com.devoteam.accesscontrolservice.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING, uses = UserMapper.class)
public interface ProfileMapper {

  Profile mapToProfile(ProfileCreationRequest creationRequest);

  ProfileCreationResponse mapToProfileCreationResponse(Profile profile);

  ProfileReadResponse mapToProfileReadResponse(Profile profile);
}