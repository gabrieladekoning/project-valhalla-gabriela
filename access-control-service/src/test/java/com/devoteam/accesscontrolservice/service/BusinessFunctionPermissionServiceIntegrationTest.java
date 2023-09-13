package com.devoteam.accesscontrolservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.devoteam.accesscontrolservice.entity.BusinessFunction;
import com.devoteam.accesscontrolservice.entity.BusinessFunctionPermission;
import com.devoteam.accesscontrolservice.entity.Permission;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.repository.BusinessFunctionPermissionRepository;
import com.devoteam.accesscontrolservice.repository.BusinessFunctionRepository;
import com.devoteam.accesscontrolservice.repository.PermissionRepository;
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
class BusinessFunctionPermissionServiceIntegrationTest {

  @Autowired
  private BusinessFunctionPermissionService businessFunctionPermissionService;

  @Autowired
  private BusinessFunctionPermissionRepository businessFunctionPermissionRepository;

  @Autowired
  private BusinessFunctionRepository businessFunctionRepository;

  @Autowired
  private PermissionRepository permissionRepository;


  private BusinessFunctionPermission businessFunctionPermission;

  private BusinessFunction businessFunction;
  private Permission permission;

  @BeforeEach
  void setUp() {
    businessFunction = BusinessFunction.builder()
        .id(UUID.randomUUID())
        .applicationName("Another Application Name")
        .functionName("Another Function Name")
        .build();
    businessFunction = businessFunctionRepository.save(businessFunction);

    permission = Permission.builder()
        .id(UUID.randomUUID())
        .name("Another Permission Name")
        .build();
    permission = permissionRepository.save(permission);

    businessFunctionPermission = BusinessFunctionPermission.builder()
        .id(UUID.randomUUID())
        .businessFunction(businessFunction)
        .permission(permission)
        .build();

    businessFunctionPermission = businessFunctionPermissionRepository.save(businessFunctionPermission);
  }

  @DisplayName("Ensure transactional behavior when cascade delete fails")
  @Test
  void testCascadeDeleteBusinessFunctionPermission_TransactionalBehavior() {
    var initialBusinessFunctionPermissionCount = businessFunctionPermissionRepository.count();

    assertThrows(ResourceNotFoundException.class,
        () -> businessFunctionPermissionService.cascadeDeleteById(UUID.randomUUID()));

    var afterBusinessFunctionPermissionCount = businessFunctionPermissionRepository.count();

    assertEquals(initialBusinessFunctionPermissionCount, afterBusinessFunctionPermissionCount);
  }
}