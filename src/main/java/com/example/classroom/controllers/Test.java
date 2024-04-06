package com.example.classroom.controllers;


import com.example.classroom.entities.User;
import com.example.classroom.service.assignment.AssignmentServiceImpl;
import com.example.classroom.service.github.WebhookService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class Test {
  @Value("${github.organization.name}")
  private String name;
  @Autowired
  private GitHub gitHub;
  @Autowired
  AssignmentServiceImpl service;
  @Autowired
  WebhookService webhookService;
  @GetMapping("/")
  public ResponseEntity<?> hello(){
    return new ResponseEntity<>("worked", HttpStatus.OK);
  }
  @GetMapping("/course")
  public void course(@AuthenticationPrincipal User user) throws IOException {
    System.out.println(user.getGitHubUsername());
    service.acceptAssigment(1L, user);
  }
  @PostMapping("/webhook")
  public void test(@RequestHeader("X-GitHub-Event") String eventType,
                   @RequestBody String body)  {
    JSONObject jsonObject = new JSONObject(body);
    webhookService.processGitHubHookEventData(jsonObject);
  }
}
