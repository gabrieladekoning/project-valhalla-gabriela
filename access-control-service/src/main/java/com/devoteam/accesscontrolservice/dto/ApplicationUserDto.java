package com.devoteam.accesscontrolservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class ApplicationUserDto {
  private UUID id;
  private String firstName;
  private String lastName;
  private String email;

}
