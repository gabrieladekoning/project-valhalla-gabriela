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
@Table(name = "business_function_permissions")
public class BusinessFunctionPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID businessFunctionPermissionId;
    @ManyToOne
    @JoinColumn(name = "business_function_id", nullable = false)
    private BusinessFunction businessFunction;
    @ManyToOne
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;
    @OneToMany(mappedBy = "businessFunctionPermission")
    private List<ProfileBusinessFunctionPermission> profiles;
}