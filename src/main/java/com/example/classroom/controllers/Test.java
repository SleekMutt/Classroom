package com.example.classroom.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class Test {
  @GetMapping("/")
  public ResponseEntity<?> hello(){
    return new ResponseEntity<>("worked", HttpStatus.OK);
  }

}
