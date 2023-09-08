package com.devoteam.accesscontrolservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class ProfileUpdateRequest {

  @NotNull(message = "Id cannot be null")
  private String id;
  @NotBlank(message = "Permission name is required")
  private String name;
}
