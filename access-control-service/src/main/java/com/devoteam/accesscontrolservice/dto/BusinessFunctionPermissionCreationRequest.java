package com.devoteam.accesscontrolservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class BusinessFunctionPermissionCreationRequest {
    @NotBlank(message = "BusinessFunction ID is required")
    private UUID businessFunctionId;
    @NotBlank(message = "Permission ID is required")
    private UUID permissionId;
}
