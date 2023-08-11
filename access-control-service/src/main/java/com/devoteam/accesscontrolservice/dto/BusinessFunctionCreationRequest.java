package com.devoteam.accesscontrolservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class BusinessFunctionCreationRequest {
    @NotBlank(message = "Application name is required")
    private String applicationName;
    @NotBlank(message = "Function name is required")
    private String functionName;
}
