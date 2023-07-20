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
public class ProfileBusinessFunctionPermissionCreationRequest {
    @NotBlank(message = "BusinessFunctionPermission ID is required")
    private String businessFunctionPermissionId;
    @NotBlank(message = "Profile ID is required")
    private String profileId;
}