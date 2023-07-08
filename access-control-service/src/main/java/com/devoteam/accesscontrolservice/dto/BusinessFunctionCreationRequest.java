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
public class BusinessFunctionCreationRequest {
    @NotBlank(message = "Application name is required")
    private String applicationName;
    @NotBlank(message = "Function name is required")
    private String functionName;
}
