package com.example.classroom.config.controllers;


import com.example.classroom.config.jwt.JwtService;
import com.example.classroom.entities.User;
import com.example.classroom.service.UserServiceDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class Test {
  @Autowired
  AuthenticationManager authenticationManager;
  @Autowired
  JwtService jwtUtils;
  @Autowired
  UserServiceDetailsImpl userServiceDetails;

  @PostMapping("/auth")
  public ResponseEntity<String> signUp(@RequestBody User user) {
    Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    User userDetails = (User) authentication.getPrincipal();

    String jwt = jwtUtils.generateToken(userDetails);
    System.out.println(jwt);

    return new ResponseEntity<>(jwt, HttpStatus.OK);
  }
  @GetMapping("/")
  public ResponseEntity<?> hello(){
    return new ResponseEntity<>("worked", HttpStatus.OK);
  }

}
