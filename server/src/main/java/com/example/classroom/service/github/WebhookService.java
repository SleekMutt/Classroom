package com.example.classroom.service.github;

import com.example.classroom.service.user.UserServiceImpl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebhookService {
  @Autowired
  private UserServiceImpl userService;

  public void processGitHubHookEventData(JSONObject body) {
    switch (body.getString("action")){
      case "member_added":
        processAddingUserToOrganization(body);
        break;
      case "member_removed":
        processRemovingUserFromOrganization(body);
        break;
    }
  }
  private void processAddingUserToOrganization(JSONObject body){
    JSONObject membership = body.getJSONObject("membership");
    JSONObject user = membership.getJSONObject("user");
    userService.updateActivationFlagByUsername(user.getString("login"), true);
  }
  private void processRemovingUserFromOrganization(JSONObject body){
    JSONObject membership = body.getJSONObject("membership");
    JSONObject user = membership.getJSONObject("user");
    userService.updateActivationFlagByUsername(user.getString("login"), false);
  }
}
