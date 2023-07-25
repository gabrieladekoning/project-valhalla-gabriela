package com.devoteam.accesscontrolservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class UserProfileCreationResponse {
    private String userProfileId;
}