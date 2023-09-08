package com.devoteam.accesscontrolservice.repository;

import com.devoteam.accesscontrolservice.entity.BusinessFunction;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BusinessFunctionRepository extends JpaRepository<BusinessFunction, UUID> {

  Optional<BusinessFunction> findByApplicationNameAndFunctionName(String applicationName, String functionName);

  @Query("SELECT bf FROM BusinessFunction bf WHERE EXISTS (SELECT bfp FROM BusinessFunctionPermission bfp "
      + "WHERE bfp.businessFunction.id = bf.id) AND bf.id = :id")
  Optional<BusinessFunction> findBusinessFunctionWithPermissionById(UUID id);
}