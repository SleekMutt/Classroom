package com.example.classroom.controllers;

import com.example.classroom.service.auth.Oauth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
  private final Oauth2Service oauth2Service;


  @GetMapping("/github/authorize")
  public ResponseEntity<String> authorize() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Location", "https://github.com/login/oauth/authorize?client_id=" + oauth2Service.getClientId());
    return new ResponseEntity<>(headers, HttpStatus.FOUND);
  }
  @GetMapping("/github/callback")
  public String callback(@RequestParam("code") String code) {
    String accessToken = oauth2Service.getAccessToken(code);
    return "redirect:http://localhost:3000?accessToken=" + accessToken;
  }
}
