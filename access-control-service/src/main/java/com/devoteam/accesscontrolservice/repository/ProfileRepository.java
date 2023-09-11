package com.devoteam.accesscontrolservice.repository;

import com.devoteam.accesscontrolservice.entity.Profile;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {

  Optional<Profile> findByName(String name);

  @Query("SELECT p FROM Profile p WHERE EXISTS (SELECT up FROM UserProfile up WHERE up.profile.id = p.id) AND p.id = :id")
  Optional<Profile> findProfileWithAssociationsById(UUID id);

  @Modifying
  @Query(value = "DELETE FROM profile_business_function_permissions WHERE profile_id = ?1", nativeQuery = true)
  void deleteProfileBusinessFunctionPermissionsByProfileId(UUID profileId);

  @Modifying
  @Query("DELETE FROM UserProfile up WHERE up.profile.id = ?1")
  void deleteUserProfileByProfileId(UUID profileId);
}