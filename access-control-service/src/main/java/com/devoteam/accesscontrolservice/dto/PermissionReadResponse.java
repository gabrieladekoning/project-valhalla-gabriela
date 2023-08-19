package com.devoteam.accesscontrolservice.dto;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class PermissionReadResponse {

  private String id;
  private String name;
}
