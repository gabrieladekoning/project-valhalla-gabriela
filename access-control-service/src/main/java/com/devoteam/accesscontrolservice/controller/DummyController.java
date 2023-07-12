package com.devoteam.accesscontrolservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class DummyController {
  @GetMapping(path = "/names")
  @Operation(security = {@SecurityRequirement(name = "bearerAuth")},
      summary = "Dummy endpoint which returns names")
  public ResponseEntity<List<String>> getNames() {
    return ResponseEntity.ok(List.of("Tofu", "Taro", "Sprout"));
  }
}