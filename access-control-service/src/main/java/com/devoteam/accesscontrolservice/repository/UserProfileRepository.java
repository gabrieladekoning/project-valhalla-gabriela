package com.devoteam.accesscontrolservice.repository;

import com.devoteam.accesscontrolservice.entity.ApplicationUser;
import com.devoteam.accesscontrolservice.entity.Profile;
import com.devoteam.accesscontrolservice.entity.UserProfile;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

  Optional<UserProfile> findByApplicationUserAndProfile(ApplicationUser applicationUser, Profile profile);

  @Query("SELECT u FROM UserProfile u JOIN FETCH u.applicationUser JOIN FETCH u.profile")
  Page<UserProfile> findAllWithApplicationUserAndProfile(Pageable pageable);
}