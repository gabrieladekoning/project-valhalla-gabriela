package com.devoteam.accesscontrolservice.repository;

import com.devoteam.accesscontrolservice.entity.BusinessFunction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BusinessFunctionRepository extends JpaRepository<BusinessFunction, UUID> {
    Optional<BusinessFunction> findByApplicationNameAndFunctionName(String applicationName, String functionName);
}
