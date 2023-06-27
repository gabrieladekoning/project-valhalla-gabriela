package com.devoteam.accesscontrolservice.entity;

import jakarta.persistence.*;

import java.util.UUID;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "application_user")
public class ApplicationUser {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;
  private String firstName;
  private String lastName;
  private String email;

}
