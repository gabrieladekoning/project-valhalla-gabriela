package com.devoteam.accesscontrolservice.dto;

import java.util.List;
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
public class ProfileWithUsersReadResponse {

  private String id;
  private String name;
  private List<ApplicationUserReadResponse> users;
}