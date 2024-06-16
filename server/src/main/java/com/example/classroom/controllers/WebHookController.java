package com.example.classroom.controllers;

import com.example.classroom.service.github.WebhookService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebHookController {
  @Autowired
  private WebhookService webhookService;
  @PostMapping("/organization")
  public void organizationEvent(@RequestHeader("X-GitHub-Event") String eventType,
                   @RequestBody String body)  {
    JSONObject jsonObject = new JSONObject(body);
    webhookService.processGitHubHookEventData(jsonObject);
  }
  @PostMapping("/git-push")
  public void gitPushEvent(@RequestHeader("X-GitHub-Event") String eventType,
                   @RequestBody String body)  {
    JSONObject jsonObject = new JSONObject(body);
    webhookService.processGitHubHookEventData(jsonObject);
  }
}
