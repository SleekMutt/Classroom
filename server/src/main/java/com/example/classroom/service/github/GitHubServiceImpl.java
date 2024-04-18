package com.example.classroom.service.github;

import com.example.classroom.entities.User;
import com.example.classroom.exception.UserOrganizationAbsenceException;
import org.kohsuke.github.GHCreateRepositoryBuilder;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GitHubServiceImpl implements IGitHubService{
  @Autowired
  private GitHub gitHub;
  @Autowired
  private GHOrganization organization;

  @Override
  public String createRepository(String assignmentCourseName, User mentor, User student) {
    try {
      String repositoryName = assignmentCourseName + "-" + student.getGitHubUsername();
      GHCreateRepositoryBuilder repository = organization.createRepository(repositoryName);
      repository.private_(true);
      GHRepository ghRepository = repository.create();
      GHUser mentorGh = gitHub.getUser(mentor.getGitHubUsername());
      GHUser studentGh = gitHub.getUser(student.getGitHubUsername());
      try {
        if(!organization.hasMember(mentorGh)){
          inviteUserToOrganization(mentor.getGitHubUsername());
          throw   new UserOrganizationAbsenceException("User " + mentor.getGitHubUsername() + " is not part of the organization"
                  + "Please accept invitation to be part of the organization");
        }
        if(!organization.hasMember(studentGh)){
          inviteUserToOrganization(student.getGitHubUsername());
          throw new UserOrganizationAbsenceException("User " + student.getGitHubUsername() + " is not part of the organization"
                  + "Please accept invitation to be part of the organization");
        }
        ghRepository.addCollaborators(GHOrganization.RepositoryRole.from(GHOrganization.Permission.PUSH),mentorGh);
        ghRepository.addCollaborators(GHOrganization.RepositoryRole.from(GHOrganization.Permission.PUSH), studentGh);
        return repositoryName;
      }
      catch (UserOrganizationAbsenceException e){
        ghRepository.delete();
        throw e;
      }
      catch (IOException e) {
        ghRepository.delete();
        throw new RuntimeException(e);
      }

    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  public void inviteUserToOrganization(String username){
    try {
      organization.add(gitHub.getUser(username), GHOrganization.Role.MEMBER);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
