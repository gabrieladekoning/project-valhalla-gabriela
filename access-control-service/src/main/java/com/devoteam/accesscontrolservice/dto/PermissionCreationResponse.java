package com.devoteam.accesscontrolservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class PermissionCreationResponse {
    private UUID id;
}
