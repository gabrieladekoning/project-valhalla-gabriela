package com.devoteam.accesscontrolservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.devoteam.accesscontrolservice.entity.BusinessFunction;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.repository.BusinessFunctionRepository;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class BusinessFunctionServiceIntegrationTest {

  @Autowired
  private BusinessFunctionService businessFunctionService;

  @Autowired
  private BusinessFunctionRepository businessFunctionRepository;

  private BusinessFunction businessFunction;

  @BeforeEach
  void setUp() {
    businessFunction = BusinessFunction.builder()
        .id(UUID.randomUUID())
        .applicationName("Application name")
        .functionName("Function name")
        .build();
    businessFunctionRepository.save(businessFunction);
  }

  @DisplayName("Ensure transactional behavior when cascade delete fails")
  @Test
  void testCascadeDeleteBusinessFunction_TransactionalBehavior() {
    var initialBusinessFunctionCount = businessFunctionRepository.count();

    assertThrows(ResourceNotFoundException.class, () -> businessFunctionService.cascadeDeleteById(UUID.randomUUID()));

    var afterBusinessFunctionCount = businessFunctionRepository.count();

    assertEquals(initialBusinessFunctionCount, afterBusinessFunctionCount);
  }
}
