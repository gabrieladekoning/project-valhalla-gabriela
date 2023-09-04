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
public class BusinessFunctionUpdateRequest {

  @NotNull(message = "Id cannot be null")
  private String id;
  @NotBlank(message = "Application name is required")
  private String applicationName;
  @NotBlank(message = "Function name is required")
  private String functionName;
}
