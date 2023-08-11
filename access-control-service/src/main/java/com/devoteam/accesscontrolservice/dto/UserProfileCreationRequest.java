package com.devoteam.accesscontrolservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserProfileCreationRequest {
    @NotBlank(message = "User ID is required")
    private String applicationUserId;
    @NotBlank(message = "Profile ID is required")
    private String profileId;
}