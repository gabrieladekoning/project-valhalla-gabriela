package com.devoteam.accesscontrolservice.mapper;

import com.devoteam.accesscontrolservice.dto.UserProfileCreationRequest;
import com.devoteam.accesscontrolservice.dto.UserProfileCreationResponse;
import com.devoteam.accesscontrolservice.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfileMapper INSTANCE = Mappers.getMapper(UserProfileMapper.class);

    @Mapping(source = "applicationUserId", target = "applicationUser.id")
    @Mapping(source = "profileId", target = "profile.id")
    UserProfile mapToUserProfile(UserProfileCreationRequest creationRequest);

    UserProfileCreationResponse mapToUserProfileResponse(UserProfile userProfile);
}