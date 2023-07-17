package com.devoteam.accesscontrolservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class BusinessFunctionPermissionCreationRequest {
    @NotNull(message = "BusinessFunction ID is required")
    private String businessFunctionId;
    @NotNull(message = "Permission ID is required")
    private String permissionId;
}
