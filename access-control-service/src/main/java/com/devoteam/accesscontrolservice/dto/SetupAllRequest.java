package com.devoteam.accesscontrolservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class SetupAllRequest {

  @NotBlank(message = "Application name is required")
  private String applicationName;
  @NotBlank(message = "Function name is required")
  private String functionName;
  @NotBlank(message = "Permission name is required")
  private String permissionName;
  @NotBlank(message = "Profile name is required")
  private String profileName;
  @NotBlank(message = "Email is required")
  @Email(message = "Email format is invalid")
  private String email;
}
