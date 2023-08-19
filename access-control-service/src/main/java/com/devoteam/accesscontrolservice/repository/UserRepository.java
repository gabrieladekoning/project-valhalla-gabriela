package com.devoteam.accesscontrolservice.repository;

import com.devoteam.accesscontrolservice.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<ApplicationUser, UUID> {

  Optional<ApplicationUser> findByEmail(String email);

  @Query(value =
      "SELECT COUNT(*) > 0 " +
          "FROM application_users u " +
          "JOIN user_profiles up ON u.id = up.application_user_id " +
          "JOIN profiles p ON up.profile_id = p.id " +
          "JOIN profile_business_function_permissions pbfp ON p.id = pbfp.profile_id " +
          "JOIN business_function_permissions bfp ON pbfp.business_function_permission_id = bfp.id " +
          "JOIN business_functions bf ON bfp.business_function_id = bf.id " +
          "JOIN permissions perm ON bfp.permission_id = perm.id " +
          "WHERE u.id = :userUUid " +
          "AND bf.application_name = :applicationName " +
          "AND bf.function_name = :functionName " +
          "AND perm.name = :permissionName",
      nativeQuery = true)
  Long checkUserAccess(@Param("userUUid") UUID userUUid,
      @Param("applicationName") String applicationName,
      @Param("functionName") String functionName,
      @Param("permissionName") String permissionName);
}