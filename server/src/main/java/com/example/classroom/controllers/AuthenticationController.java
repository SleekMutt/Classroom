package com.example.classroom.controllers;

import com.example.classroom.service.auth.Oauth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
  @Autowired
  private Oauth2Service oauth2Service;


  @GetMapping("/github/authorize")
  public String authorize() {
    return "redirect:https://github.com/login/oauth/authorize?client_id=" + oauth2Service.getClientId();
  }
  @GetMapping("/github/callback")
  public String callback(@RequestParam("code") String code) {
    String accessToken = oauth2Service.getAccessToken(code);
    return "redirect:/?accessToken=" + accessToken;
  }
}
