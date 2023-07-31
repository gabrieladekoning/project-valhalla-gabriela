package com.devoteam.accesscontrolservice.repository;

import com.devoteam.accesscontrolservice.entity.ApplicationUser;
import com.devoteam.accesscontrolservice.entity.Profile;
import com.devoteam.accesscontrolservice.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    Optional<UserProfile> findByApplicationUserAndProfile(ApplicationUser applicationUser, Profile profile);
    Optional<UserProfile> findByApplicationUser(ApplicationUser applicationUser);
}