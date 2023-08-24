package com.devoteam.accesscontrolservice.dto;

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
public class ProfileBusinessFunctionPermissionReadResponse {

  private String id;
  private BusinessFunctionPermissionReadResponse businessFunctionPermission;
  private ProfileReadResponse profile;
}