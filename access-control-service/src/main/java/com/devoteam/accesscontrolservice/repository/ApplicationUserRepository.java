package com.devoteam.accesscontrolservice.repository;

import com.devoteam.accesscontrolservice.entity.ApplicationUser;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationUserRepository extends JpaRepository <ApplicationUser, UUID> {

}
