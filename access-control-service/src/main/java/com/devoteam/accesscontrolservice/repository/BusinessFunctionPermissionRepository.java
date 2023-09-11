package com.devoteam.accesscontrolservice.repository;

import com.devoteam.accesscontrolservice.entity.BusinessFunction;
import com.devoteam.accesscontrolservice.entity.BusinessFunctionPermission;
import com.devoteam.accesscontrolservice.entity.Permission;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BusinessFunctionPermissionRepository extends JpaRepository<BusinessFunctionPermission, UUID> {

  Optional<BusinessFunctionPermission> findByBusinessFunctionAndPermission(BusinessFunction businessFunction,
      Permission permission);

  Page<BusinessFunctionPermission> findByBusinessFunction_ApplicationName(String applicationName, Pageable pageable);

  @Query(
      "SELECT bfp FROM BusinessFunctionPermission bfp WHERE EXISTS (SELECT pbfp FROM ProfileBusinessFunctionPermission pbfp "
          + "WHERE pbfp.businessFunctionPermission.id = bfp.id) AND bfp.id = :id")
  Optional<BusinessFunctionPermission> findBusinessFunctionPermissionWithProfileById(UUID id);

  @Modifying
  @Query(value = "DELETE FROM profile_business_function_permissions "
      + "WHERE business_function_permission_id = ?1", nativeQuery = true)
  void deleteProfileBusinessFunctionPermissionsByBusinessFunctionPermissionId(UUID id);
}