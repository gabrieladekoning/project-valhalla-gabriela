package com.devoteam.accesscontrolservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class UserProfileCreationRequest {
    @NotBlank(message = "User ID is required")
    private String applicationUserId;
    @NotBlank(message = "Profile ID is required")
    private String profileId;
}