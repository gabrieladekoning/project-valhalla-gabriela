package com.devoteam.accesscontrolservice.exception;

public class UserCreationFailedException extends RuntimeException {

  public UserCreationFailedException(String message) {
    super(message);
  }
}