package com.example.classroom.service.auth;

import com.example.classroom.config.security.jwt.JwtService;
import com.example.classroom.dto.GithubTokenRequest;
import com.example.classroom.entities.User;
import com.example.classroom.service.user.UserServiceImpl;
import lombok.Getter;
import org.kohsuke.github.GHMyself;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class Oauth2Service {
  @Autowired
  private UserServiceImpl userServiceImpl;
  @Autowired
  private JwtService jwtService;
  @Autowired
  private GHOrganization organization;

  @Getter
  @Value("${spring.security.oauth2.client.registration.github.client-id}")
  private String clientId;
  @Value("${spring.security.oauth2.client.registration.github.client-secret}")
  private String clientSecret;

  //TODO Виділити інвайт в організацію в окремий функціонал
  public String getAccessToken(String code) {
    String accessTokenUri = "https://github.com/login/oauth/access_token" +
            "?client_id=" + clientId +
            "&client_secret=" + clientSecret +
            "&code=" + code;
    GithubTokenRequest gitHubAccessToken = new RestTemplate().postForObject(accessTokenUri, null, GithubTokenRequest.class);
    try {
      GitHub github = new GitHubBuilder().withOAuthToken(gitHubAccessToken.getAccessToken()).build();
      GHMyself myself = github.getMyself();
      organization.add(myself, GHOrganization.Role.MEMBER);
      if (!userServiceImpl.existsUserByUsername(myself.getLogin())) {
        registerUser(myself.getLogin(), gitHubAccessToken.getAccessToken());
      }
      else{
        userServiceImpl.updateTokenByUsername(myself.getLogin(), gitHubAccessToken.getAccessToken());
      }

      UserDetails userDetails = userServiceImpl.userDetailsService().loadUserByUsername(myself.getLogin());
      return jwtService.generateToken(userDetails);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void registerUser(String login, String token){
    User user = new User();
    user.setRole("User");
    user.setGitHubToken(token);
    user.setGitHubUsername(login);
    userServiceImpl.createUser(user);
  }
}
