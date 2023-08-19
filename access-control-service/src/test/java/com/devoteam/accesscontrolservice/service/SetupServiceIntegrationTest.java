package com.devoteam.accesscontrolservice.service;

import com.devoteam.accesscontrolservice.dto.ProfileCreationRequest;
import com.devoteam.accesscontrolservice.dto.SetupAllRequest;
import com.devoteam.accesscontrolservice.entity.ApplicationUser;
import com.devoteam.accesscontrolservice.entity.BusinessFunction;
import com.devoteam.accesscontrolservice.entity.Permission;
import com.devoteam.accesscontrolservice.entity.Profile;
import com.devoteam.accesscontrolservice.repository.BusinessFunctionRepository;
import com.devoteam.accesscontrolservice.repository.PermissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class SetupServiceIntegrationTest {

  @Autowired
  private SetupService setupService;

  @Autowired
  private BusinessFunctionRepository businessFunctionRepository;

  @Autowired
  private PermissionRepository permissionRepository;

  @MockBean
  ProfileService profileService;

  private SetupAllRequest setupAllRequest;

  @BeforeEach
  void setUp() {
    var applicationUser = ApplicationUser.builder()
        .id(UUID.randomUUID())
        .email("test@email.com")
        .build();
    var businessFunction = BusinessFunction.builder()
        .id(UUID.randomUUID())
        .applicationName("Application name")
        .functionName("Function name")
        .build();
    var permission = Permission.builder()
        .id(UUID.randomUUID())
        .name("Permission name")
        .build();
    var profile = Profile.builder()
        .id(UUID.randomUUID())
        .name("Profile name")
        .build();

    setupAllRequest = SetupAllRequest.builder()
        .applicationName(businessFunction.getApplicationName())
        .functionName(businessFunction.getFunctionName())
        .permissionName(permission.getName())
        .profileName(profile.getName())
        .email(applicationUser.getEmail())
        .build();
  }

  @DisplayName("Ensure transactional behavior when profile creation/retrieval fails")
  @Test
  void testSetupAll_TransactionalBehavior() {
    doThrow(new RuntimeException("This is a Runtime Exception which will definitely not ruin everything"))
        .when(profileService).createOrRetrieveProfile(any(ProfileCreationRequest.class));

    var initialBusinessFunctionCount = businessFunctionRepository.count();
    var initialPermissionCount = permissionRepository.count();

    assertThrows(RuntimeException.class, () -> setupService.setupAll(setupAllRequest));

    var afterBusinessFunctionCount = businessFunctionRepository.count();
    var afterPermissionCount = permissionRepository.count();

    assertEquals(initialBusinessFunctionCount, afterBusinessFunctionCount);
    assertEquals(initialPermissionCount, afterPermissionCount);
  }
}