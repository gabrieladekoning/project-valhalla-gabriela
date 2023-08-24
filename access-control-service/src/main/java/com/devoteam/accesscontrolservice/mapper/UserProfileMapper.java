package com.devoteam.accesscontrolservice.mapper;

import com.devoteam.accesscontrolservice.dto.ProfileWithUsersReadResponse;
import com.devoteam.accesscontrolservice.dto.UserIdAndEmailReadResponse;
import com.devoteam.accesscontrolservice.dto.UserProfileCreationRequest;
import com.devoteam.accesscontrolservice.dto.UserProfileCreationResponse;
import com.devoteam.accesscontrolservice.entity.Profile;
import com.devoteam.accesscontrolservice.entity.UserProfile;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface UserProfileMapper {

  @Mapping(source = "applicationUserId", target = "applicationUser.id")
  @Mapping(source = "profileId", target = "profile.id")
  UserProfile mapToUserProfile(UserProfileCreationRequest creationRequest);

  UserProfileCreationResponse mapToUserProfileCreationResponse(UserProfile userProfile);

  @Mapping(source = "applicationUser.id", target = "id")
  @Mapping(source = "applicationUser.email", target = "email")
  UserIdAndEmailReadResponse mapToUserReadResponse(UserProfile userProfile);

  ProfileWithUsersReadResponse mapToProfileWithUsersReadResponse(Profile profile, List<UserIdAndEmailReadResponse> users);
}