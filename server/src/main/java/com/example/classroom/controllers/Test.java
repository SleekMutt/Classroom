package com.example.classroom.controllers;


import com.example.classroom.entities.Notification;
import com.example.classroom.entities.User;
import com.example.classroom.repository.NotificationRepository;
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
import java.time.LocalDateTime;


@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class Test {
  @Autowired
  NotificationRepository repository;

  @GetMapping("/")
  public void hello(){
    repository.save(Notification.builder().message("test").sentDate(LocalDateTime.now()).user(User.builder().gitHubUsername("SleekMutt").id(1L).build()).build());
  }


}
