package com.devoteam.accesscontrolservice.repository;

import com.devoteam.accesscontrolservice.entity.Permission;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {

  Optional<Permission> findByName(String name);

  @Query("SELECT p FROM Permission p WHERE EXISTS (SELECT bfp FROM BusinessFunctionPermission bfp "
      + "WHERE bfp.permission.id = p.id) AND p.id = :id")
  Optional<Permission> findPermissionWithBusinessFunctionPermissionById(UUID id);

  @Modifying
  @Query(value = "DELETE FROM profile_business_function_permissions WHERE business_function_permission_id IN "
      + "(SELECT id FROM business_function_permissions WHERE permission_id = ?1)", nativeQuery = true)
  void deleteProfileBusinessFunctionPermissionsByPermissionId(UUID id);

  @Modifying
  @Query("DELETE FROM BusinessFunctionPermission bfp WHERE bfp.permission.id = ?1")
  void deleteBusinessFunctionPermissionsByPermissionId(UUID id);
}