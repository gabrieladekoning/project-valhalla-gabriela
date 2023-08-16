package com.devoteam.accesscontrolservice.dto;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class BusinessFunctionPermissionReadResponse {
    private String id;
    private String businessFunctionId;
    private String applicationName;
    private String functionName;
    private String permissionId;
    private String permissionName;
}
