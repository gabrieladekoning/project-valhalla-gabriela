package com.devoteam.accesscontrolservice.repository;

import com.devoteam.accesscontrolservice.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<ApplicationUser, UUID> {
    Optional<ApplicationUser> findByEmail(String email);

    @Query(value = "SELECT COUNT(u.id) > 0 " +
            "FROM ApplicationUser u " +
            "JOIN u.profiles up " +
            "JOIN up.profile p " +
            "JOIN p.businessFunctionPermissions pbfp " +
            "JOIN pbfp.businessFunctionPermission bfp " +
            "JOIN bfp.businessFunction bf " +
            "JOIN bfp.permission perm " +
            "WHERE u.id = :userUUid " +
            "AND bf.applicationName = :applicationName " +
            "AND bf.functionName = :functionName " +
            "AND perm.name = :permissionName")
    boolean checkUserAccess(@Param("userUUid") UUID userUUid,
                            @Param("applicationName") String applicationName,
                            @Param("functionName") String functionName,
                            @Param("permissionName") String permissionName);
}