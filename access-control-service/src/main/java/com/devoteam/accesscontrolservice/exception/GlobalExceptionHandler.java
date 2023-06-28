package com.devoteam.accesscontrolservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  @ExceptionHandler(value = {Exception.class})
  public ResponseEntity<Object> handleAnyException(Exception e) {
    // todo: this
    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
    log.error("Exception caught in handleMethodArgumentNotValid: {}", exception.getMessage(), exception);
    var error = exception.getBindingResult().getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .sorted()
            .collect(Collectors.joining(","));
    log.error("Error is: {}", error);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }
}
