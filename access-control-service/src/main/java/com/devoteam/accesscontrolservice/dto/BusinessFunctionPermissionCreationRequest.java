package com.devoteam.accesscontrolservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class BusinessFunctionPermissionCreationRequest {
    @NotBlank(message = "Business function ID is required")
    private String businessFunctionId;
    @NotBlank(message = "Permission ID is required")
    private String permissionId;
}