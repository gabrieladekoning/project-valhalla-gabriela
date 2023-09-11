package com.devoteam.accesscontrolservice.exception;

public class CannotDeleteResourceException extends RuntimeException {

  public CannotDeleteResourceException(String message) {
    super(message);
  }
}