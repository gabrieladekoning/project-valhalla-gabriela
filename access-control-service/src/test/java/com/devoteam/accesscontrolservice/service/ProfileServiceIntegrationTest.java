package com.devoteam.accesscontrolservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.devoteam.accesscontrolservice.entity.Profile;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.repository.ProfileRepository;
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
class ProfileServiceIntegrationTest {

  @Autowired
  private ProfileService profileService;

  @Autowired
  private ProfileRepository profileRepository;

  private Profile profile;

  @BeforeEach
  void setUp() {
    profile = Profile.builder()
        .id(UUID.randomUUID())
        .name("Profile name")
        .build();
    profileRepository.save(profile);
  }

  @DisplayName("Ensure transactional behavior when cascade delete fails")
  @Test
  void testCascadeDeleteProfile_TransactionalBehavior() {
    var initialProfileCount = profileRepository.count();

    assertThrows(ResourceNotFoundException.class, () -> profileService.cascadeDeleteById(UUID.randomUUID()));

    var afterProfileCount = profileRepository.count();

    assertEquals(initialProfileCount, afterProfileCount);
  }
}

