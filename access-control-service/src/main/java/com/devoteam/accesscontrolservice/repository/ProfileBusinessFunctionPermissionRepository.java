package com.devoteam.accesscontrolservice.repository;

import com.devoteam.accesscontrolservice.entity.BusinessFunctionPermission;
import com.devoteam.accesscontrolservice.entity.Profile;
import com.devoteam.accesscontrolservice.entity.ProfileBusinessFunctionPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfileBusinessFunctionPermissionRepository extends
    JpaRepository<ProfileBusinessFunctionPermission, UUID> {

  Optional<ProfileBusinessFunctionPermission> findByBusinessFunctionPermissionAndProfile(
      BusinessFunctionPermission businessFunctionPermission, Profile profile);

  Page<ProfileBusinessFunctionPermission> findByProfile_Name(String profileName, Pageable pageable);
}