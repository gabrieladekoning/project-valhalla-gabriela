package com.devoteam.accesscontrolservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "user_profiles")
public class UserProfile {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;
  @ManyToOne
  @JoinColumn(name = "application_user_id", nullable = false)
  private ApplicationUser applicationUser;
  @ManyToOne
  @JoinColumn(name = "profile_id", nullable = false)
  private Profile profile;
}