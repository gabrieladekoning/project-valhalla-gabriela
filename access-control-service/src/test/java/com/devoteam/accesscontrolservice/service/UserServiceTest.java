package com.devoteam.accesscontrolservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.devoteam.accesscontrolservice.entity.ApplicationUser;
import com.devoteam.accesscontrolservice.exception.EmailAlreadyExistsException;
import com.devoteam.accesscontrolservice.exception.ResourceNotFoundException;
import com.devoteam.accesscontrolservice.exception.UserCreationFailedException;
import com.devoteam.accesscontrolservice.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private KeycloakService keycloakService;
  @InjectMocks
  private UserService userService;
  private ApplicationUser user;
  private ApplicationUser secondUser;
  private String passwordToSave;

  @BeforeEach
  public void setup() {
    user = ApplicationUser.builder()
        .id(UUID.randomUUID())
        .firstName("Tofu")
        .lastName("Cat")
        .email("tofu@email.com")
        .build();

    secondUser = ApplicationUser.builder()
        .id(UUID.randomUUID())
        .firstName("Taro")
        .lastName("Cat")
        .email("taro@email.com")
        .build();

    passwordToSave = "Th!S-1s-a-p4S$w0rD-t0-u$e";
  }

  @DisplayName("Saving a new user successfully")
  @Test
  void saveApplicationUser_SuccessfullySaved_WhenUserDoesNotAlreadyExist() {
    given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.empty());
    given(keycloakService.createUser(user, passwordToSave)).willReturn(String.valueOf(user.getId()));
    given(userRepository.save(any())).willReturn(user);

    var savedUser = userService.saveApplicationUser(user, passwordToSave);
    savedUser.setId(user.getId());

    Assertions.assertThat(savedUser).isNotNull();
  }

  @DisplayName("Saving a user with existing email")
  @Test
  void saveApplicationUser_ThrowsEmailAlreadyExistsException_WhenEmailAlreadyExists() {
    given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

    assertThrows(EmailAlreadyExistsException.class, () -> userService.saveApplicationUser(user, passwordToSave));
    then(userRepository).should(times(1)).findByEmail(user.getEmail());
    then(userRepository).should(times(0)).save(any(ApplicationUser.class));
  }

  @DisplayName("Saving a user when Keycloak registration fails")
  @Test
  void saveApplicationUser_ThrowsUserCreationException_WhenKeycloakRegistrationFails() {
    given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.empty());
    given(keycloakService.createUser(user, passwordToSave)).willThrow(
        new UserCreationFailedException("Failed to create user in Keycloak"));

    assertThrows(UserCreationFailedException.class, () -> userService.saveApplicationUser(user, passwordToSave));
    then(userRepository).should(times(1)).findByEmail(user.getEmail());
    then(userRepository).should(times(0)).save(any(ApplicationUser.class));
  }

  @DisplayName("Handling when user is not found by ID")
  @Test
  void findByIdOrThrowNotFound_ThrowsResourceNotFoundException_WhenUserNotFound() {
    given(userRepository.findById(user.getId())).willReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class,
        () -> userService.findByIdOrThrowNotFound(user.getId()));
  }

  @DisplayName("Handling when user is not found by email")
  @Test
  void findByEmailOrThrowNotFound_ThrowsResourceNotFoundException_WhenUserNotFound() {
    given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class,
        () -> userService.findByEmailOrThrowNotFound(user.getEmail()));
  }

  @Test
  @DisplayName("Returning a pageable list of users by searching first name")
  void searchAndListUsers_ReturnListOfUsersByFirstNameInsidePageObject_WhenSuccessful() {
    List<ApplicationUser> expectedUsers = new ArrayList<>();
    expectedUsers.add(user);

    var pageable = PageRequest.of(0, 5);
    var userPage = new PageImpl<>(expectedUsers, pageable, expectedUsers.size());

    when(userRepository.findByFirstNameContainingOrLastNameContainingOrEmailContaining(user.getFirstName(), null, null,
        pageable))
        .thenReturn(userPage);

    var resultUserPage = userService.searchAndListUsers(user.getFirstName(), null, null, pageable);

    verify(userRepository).findByFirstNameContainingOrLastNameContainingOrEmailContaining(user.getFirstName(), null,
        null,
        pageable);

    assertThat(resultUserPage).isNotNull();
    assertThat(resultUserPage.toList()).isNotEmpty();
    assertThat(resultUserPage.toList().get(0).getFirstName()).isEqualTo(user.getFirstName());
  }

  @Test
  @DisplayName("Returning a pageable list of users by searching last name")
  void searchAndListUsers_ReturnListOfUsersByLastNameInsidePageObject_WhenSuccessful() {
    List<ApplicationUser> expectedUsers = new ArrayList<>();
    expectedUsers.add(user);
    expectedUsers.add(secondUser);

    var pageable = PageRequest.of(0, 5);
    var userPage = new PageImpl<>(expectedUsers, pageable, expectedUsers.size());

    when(userRepository.findByFirstNameContainingOrLastNameContainingOrEmailContaining(null, user.getLastName(), null,
        pageable))
        .thenReturn(userPage);

    var resultUserPage = userService.searchAndListUsers(null, user.getLastName(), null, pageable);

    verify(userRepository).findByFirstNameContainingOrLastNameContainingOrEmailContaining(null, user.getLastName(),
        null,
        pageable);

    assertThat(resultUserPage).isNotNull();
    assertThat(resultUserPage.toList()).isNotEmpty();
    assertThat(resultUserPage.toList().get(0).getLastName()).isEqualTo(user.getLastName());
    assertThat(resultUserPage.toList().get(1).getLastName()).isEqualTo(secondUser.getLastName());
  }

  @Test
  @DisplayName("Returning a pageable list of users by searching email")
  void searchAndListUsers_ReturnListOfUsersByEmailInsidePageObject_WhenSuccessful() {
    List<ApplicationUser> expectedUsers = new ArrayList<>();
    expectedUsers.add(user);

    var pageable = PageRequest.of(0, 5);
    var userPage = new PageImpl<>(expectedUsers, pageable, expectedUsers.size());

    when(userRepository.findByFirstNameContainingOrLastNameContainingOrEmailContaining(null, null, user.getEmail(),
        pageable))
        .thenReturn(userPage);

    var resultUserPage = userService.searchAndListUsers(null, null, user.getEmail(), pageable);

    verify(userRepository).findByFirstNameContainingOrLastNameContainingOrEmailContaining(null, null, user.getEmail(),
        pageable);

    assertThat(resultUserPage).isNotNull();
    assertThat(resultUserPage.toList()).isNotEmpty();
    assertThat(resultUserPage.toList().get(0).getEmail()).isEqualTo(user.getEmail());
  }
}