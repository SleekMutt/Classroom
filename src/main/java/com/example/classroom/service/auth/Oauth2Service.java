package com.example.classroom.service.auth;

import com.example.classroom.config.security.jwt.JwtService;
import com.example.classroom.dto.GitHubUserInfo;
import com.example.classroom.dto.GithubTokenRequest;
import com.example.classroom.entities.User;
import com.example.classroom.service.user.UserServiceImpl;
import lombok.Getter;
import org.kohsuke.github.GHOrganization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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


  public String getAccessToken(String code) {
    String accessTokenUri = "https://github.com/login/oauth/access_token" +
            "?client_id=" + clientId +
            "&client_secret=" + clientSecret +
            "&code=" + code;
    GithubTokenRequest gitHubAccessToken = new RestTemplate().postForObject(accessTokenUri, null, GithubTokenRequest.class);
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + gitHubAccessToken.getAccessToken());
    GitHubUserInfo userInfo = new RestTemplate().exchange("https://api.github.com/user", HttpMethod.GET, new HttpEntity<>(headers), GitHubUserInfo.class).getBody();
    if (!userServiceImpl.existsUserByUsername(userInfo.getLogin())) {
      registerUser(userInfo, gitHubAccessToken.getAccessToken());
    }
    else{
      userServiceImpl.updateTokenByUsername(userInfo.getLogin(), gitHubAccessToken.getAccessToken());
    }
    UserDetails userDetails = userServiceImpl.userDetailsService().loadUserByUsername(userInfo.getLogin());
    return jwtService.generateToken(userDetails);
  }

  private void registerUser(GitHubUserInfo userInfo, String token){
    User user = new User();
    user.setRole("User");
    user.setGitHubToken(token);
    user.setGitHubUsername(userInfo.getLogin());
    userServiceImpl.createUser(user);
  }
}
