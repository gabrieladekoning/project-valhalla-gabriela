package com.devoteam.accesscontrolservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.devoteam.accesscontrolservice.entity.BusinessFunction;
import com.devoteam.accesscontrolservice.entity.Permission;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
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
class PermissionServiceIntegrationTest {

  @Autowired
  private PermissionService permissionService;

  @Autowired
  private PermissionRepository permissionRepository;

  private Permission permission;

  @BeforeEach
  void setUp() {
    permission = Permission.builder()
        .id(UUID.randomUUID())
        .name("Permission name")
        .build();
    permissionRepository.save(permission);
  }

  @DisplayName("Ensure transactional behavior when cascade delete fails")
  @Test
  void testCascadeDeletePermission_TransactionalBehavior() {
    var initialPermissionCount = permissionRepository.count();

    assertThrows(ResourceNotFoundException.class, () -> permissionService.cascadeDeleteById(UUID.randomUUID()));

    var afterPermissionCount = permissionRepository.count();

    assertEquals(initialPermissionCount, afterPermissionCount);
  }
}

