package com.devoteam.accesscontrolservice.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class DummyController {
  @GetMapping(path = "/names")
  public ResponseEntity<List<String>> getNames (){
    return ResponseEntity.ok(List.of("Tofu", "Taro", "Sprout"));
  }
}