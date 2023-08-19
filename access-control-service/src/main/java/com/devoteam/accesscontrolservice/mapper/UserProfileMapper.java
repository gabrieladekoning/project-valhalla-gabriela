package com.devoteam.accesscontrolservice.mapper;

import com.devoteam.accesscontrolservice.dto.UserProfileCreationRequest;
import com.devoteam.accesscontrolservice.dto.UserProfileCreationResponse;
import com.devoteam.accesscontrolservice.dto.ApplicationUserReadResponse;
import com.devoteam.accesscontrolservice.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING, uses = ProfileMapper.class)
public interface UserProfileMapper {

  @Mapping(source = "applicationUserId", target = "applicationUser.id")
  @Mapping(source = "profileId", target = "profile.id")
  UserProfile mapToUserProfile(UserProfileCreationRequest creationRequest);

  UserProfileCreationResponse mapToUserProfileCreationResponse(UserProfile userProfile);

  @Mapping(source = "applicationUser.id", target = "id")
  @Mapping(source = "applicationUser.email", target = "email")
  ApplicationUserReadResponse mapToUserReadResponse(UserProfile userProfile);
}