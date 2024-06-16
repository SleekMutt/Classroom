package com.example.classroom.controllers;
import com.example.classroom.service.github.PullRequestValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/open-ai")
@RequiredArgsConstructor
public class OpenAiController {
  @Autowired
  private PullRequestValidationService pullRequestValidationService;

  @PostMapping("/validate")
  public ResponseEntity<?> validate(@RequestParam("repositoryName") String repositoryName) {
    pullRequestValidationService.validatePullRequest(repositoryName);
    return null;
  }

}