package com.devoteam.accesscontrolservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "business_functions")
public class BusinessFunction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID businessFunctionId;
    @Column(name = "application_name", nullable = false)
    private String applicationName;
    @Column(name = "function_name", nullable = false)
    private String functionName;
    @OneToMany(mappedBy = "businessFunction")
    private List<BusinessFunctionPermission> permissions;
}