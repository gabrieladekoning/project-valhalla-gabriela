package com.devoteam.accesscontrolservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ApplicationUserDto {
  @NotBlank(message = "First name is required")
  private String firstName;
  @NotBlank(message = "Last name is required")
  private String lastName;
  @NotBlank(message = "Email is required")
  @Email(message = "Email format is invalid")
  private String email;
}
