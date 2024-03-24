package com.example.classroom.config.github;

import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GithubConnectConfig {
  @Value("${github.organization.token}")
  private String githubToken;
  @Value("${github.organization.name}")
  private String githubOrganizationName;
  @Bean
  public GitHub gitHub() throws IOException {
    return new GitHubBuilder()
            .withOAuthToken(githubToken)
            .build();
  }
  @Bean
  public GHOrganization gitHubOrganization() throws IOException {
    return gitHub().getOrganization(githubOrganizationName);
  }
}
