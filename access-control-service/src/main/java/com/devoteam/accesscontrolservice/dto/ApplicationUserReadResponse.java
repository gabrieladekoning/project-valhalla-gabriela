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
public class ApplicationUserReadResponse {

  private String id;
  private String email;
}