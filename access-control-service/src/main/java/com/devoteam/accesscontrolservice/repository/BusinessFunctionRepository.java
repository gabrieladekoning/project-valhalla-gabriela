package com.devoteam.accesscontrolservice.repository;

import com.devoteam.accesscontrolservice.entity.BusinessFunction;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BusinessFunctionRepository extends JpaRepository<BusinessFunction, UUID> {

  Optional<BusinessFunction> findByApplicationNameAndFunctionName(String applicationName, String functionName);

  @Query("SELECT bf FROM BusinessFunction bf WHERE EXISTS (SELECT bfp FROM BusinessFunctionPermission bfp "
      + "WHERE bfp.businessFunction.id = bf.id) AND bf.id = :id")
  Optional<BusinessFunction> findBusinessFunctionWithPermissionById(UUID id);

  @Modifying
  @Query(value = "DELETE FROM profile_business_function_permissions WHERE business_function_permission_id IN "
      + "(SELECT id FROM business_function_permissions WHERE business_function_id = ?1)", nativeQuery = true)
  void deleteProfileBusinessFunctionPermissionsByBusinessFunctionId(UUID id);

  @Modifying
  @Query("DELETE FROM BusinessFunctionPermission bfp WHERE bfp.businessFunction.id = ?1")
  void deleteBusinessFunctionPermissionsByBusinessFunctionId(UUID id);
}