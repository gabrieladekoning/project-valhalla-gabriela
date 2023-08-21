package com.devoteam.accesscontrolservice.mapper;

import com.devoteam.accesscontrolservice.dto.ProfileCreationRequest;
import com.devoteam.accesscontrolservice.dto.ProfileCreationResponse;
import com.devoteam.accesscontrolservice.dto.ProfileReadResponse;
import com.devoteam.accesscontrolservice.dto.ProfileWithUsersReadResponse;
import com.devoteam.accesscontrolservice.dto.UserIdAndEmailReadResponse;
import com.devoteam.accesscontrolservice.entity.Profile;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING, uses = UserProfileMapper.class)
public interface ProfileMapper {

  Profile mapToProfile(ProfileCreationRequest creationRequest);

  ProfileCreationResponse mapToProfileCreationResponse(Profile profile);

  ProfileReadResponse mapToProfileReadResponse(Profile profile);

  ProfileWithUsersReadResponse mapToProfileWithUsersReadResponse(Profile profile, List<UserIdAndEmailReadResponse> users);
}