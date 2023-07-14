package com.devoteam.accesscontrolservice.repository;

import com.devoteam.accesscontrolservice.entity.BusinessFunction;
import com.devoteam.accesscontrolservice.entity.BusinessFunctionPermission;
import com.devoteam.accesscontrolservice.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BusinessFunctionPermissionRepository extends JpaRepository<BusinessFunctionPermission, UUID> {
    Optional<BusinessFunctionPermission> findByBusinessFunctionAndPermission(BusinessFunction businessFunction, Permission permission);
}
