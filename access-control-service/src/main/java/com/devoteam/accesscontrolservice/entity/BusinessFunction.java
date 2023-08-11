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
@Table(
        name = "business_functions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"application_name", "function_name"})
)
public class BusinessFunction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Column(name = "application_name", nullable = false)
    private String applicationName;
    @Column(name = "function_name", nullable = false)
    private String functionName;
}