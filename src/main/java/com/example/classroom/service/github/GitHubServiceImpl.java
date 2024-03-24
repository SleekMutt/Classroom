package com.example.classroom.service.github;

import com.example.classroom.entities.User;
import org.kohsuke.github.GHCreateRepositoryBuilder;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GitHubServiceImpl implements IGitHubService{
  @Autowired
  private GitHub gitHub;
  @Value("${github.organization.name}")
  private String organizationName;

  @Override
  public String createRepository(String assignmentName, User mentor, User student) {
    try {
      String repositoryName = assignmentName + "-" + student.getGitHubUsername();
      GHOrganization organization = gitHub.getOrganization(organizationName);
      GHCreateRepositoryBuilder repository = organization.createRepository(repositoryName);
      repository.private_(true);
      GHRepository ghRepository = repository.create();
      try {
        ghRepository.addCollaborators(GHOrganization.RepositoryRole.from(GHOrganization.Permission.ADMIN),gitHub.getUser(mentor.getGitHubUsername()));
        ghRepository.addCollaborators(GHOrganization.RepositoryRole.from(GHOrganization.Permission.PUSH), gitHub.getUser(student.getGitHubUsername()));
        return repositoryName;
      } catch (IOException e) {
        ghRepository.delete();
        throw new RuntimeException(e);
      }

    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
