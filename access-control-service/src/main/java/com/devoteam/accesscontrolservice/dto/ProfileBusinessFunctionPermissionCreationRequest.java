package com.devoteam.accesscontrolservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class ProfileBusinessFunctionPermissionCreationRequest {

  @NotBlank(message = "Business function permission ID is required")
  private String businessFunctionPermissionId;
  @NotBlank(message = "Profile ID is required")
  private String profileId;
}