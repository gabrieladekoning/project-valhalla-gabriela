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
@Table(name = "business_function_permissions")
public class BusinessFunctionPermission {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;
  @ManyToOne
  @JoinColumn(name = "business_function_id", nullable = false)
  private BusinessFunction businessFunction;
  @ManyToOne
  @JoinColumn(name = "permission_id", nullable = false)
  private Permission permission;
}