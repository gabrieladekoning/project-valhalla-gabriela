package com.devoteam.accesscontrolservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
    log.info("Exception caught in handleMethodArgumentNotValid: {}", exception.getMessage(), exception);
    var error = exception.getBindingResult().getAllErrors().stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .sorted()
        .collect(Collectors.joining(", "));
    log.info("Error is: {}", error);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<String> handleEmailAlreadyExists(EmailAlreadyExistsException exception) {
    log.info("Exception caught in handleEmailAlreadyExists: {}", exception.getMessage(), exception);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(UserCreationFailedException.class)
  public ResponseEntity<String> handleUserCreationException(UserCreationFailedException exception) {
    log.info("Exception caught in handleUserCreationException: {}", exception.getMessage(), exception);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException exception) {
    log.info("Exception caught in handleResourceNotFound: {}", exception.getMessage(), exception);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }

  @ExceptionHandler(DuplicateFieldException.class)
  public ResponseEntity<String> handleDuplicateName(DuplicateFieldException exception) {
    log.info("Exception caught in handleDuplicateField: {}", exception.getMessage(), exception);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(CannotDeleteResourceException.class)
  public ResponseEntity<String> handleCannotDeleteResourceException(CannotDeleteResourceException exception) {
    log.info("Exception caught in handleCannotDeleteResourceException: {}", exception.getMessage(), exception);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException exception) {
    log.info("Exception caught in handleAccessDeniedException: {}", exception.getMessage(), exception);
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleAllExceptions(Exception exception) {
    log.info("General exception occurred: {}", exception.getMessage(), exception);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
  }
}
