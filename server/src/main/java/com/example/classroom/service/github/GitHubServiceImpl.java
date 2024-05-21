package com.example.classroom.service.github;

import com.example.classroom.dto.comment.CommentDTO;
import com.example.classroom.entities.User;
import com.example.classroom.exception.UserOrganizationAbsenceException;
import org.kohsuke.github.GHBranchProtectionBuilder;
import org.kohsuke.github.GHCreateRepositoryBuilder;
import org.kohsuke.github.GHIssueComment;
import org.kohsuke.github.GHMyself;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class GitHubServiceImpl implements IGitHubService{
  @Autowired
  private GitHub gitHub;
  @Autowired
  private GHOrganization organization;

  @Override
  public String createRepository(String assignmentCourseName, User mentor, User student) {
    try {
      String repositoryName = student.getGitHubUsername() + "-" + assignmentCourseName;
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

        ghRepository.createContent()
                .branch("main")
                .path("README.md")
                .content("Welcome to " + repositoryName + "\n\nThis repository is created for " + student.getGitHubUsername() + ".")
                .message("Initial commit - Adding README")
                .commit();

/*        GHBranch mainBranch = ghRepository.getBranch("main");
        mainBranch.enableProtection()
                .restrictPushAccess()
                .enable();*/

        ghRepository.createRef("refs/heads/feedback", ghRepository.getRef("refs/heads/main").getObject().getSha());
        GHBranchProtectionBuilder protectionBuilder = ghRepository.getBranch("feedback").enableProtection();
        protectionBuilder.restrictPushAccess();
        protectionBuilder.userPushAccess(studentGh);

        ghRepository.createContent()
                .branch("feedback")
                .path("DUMMY.md")
                .content("This is a dummy file to create a difference between branches.")
                .message("Dummy commit - Adding DUMMY.md")
                .commit();

        GHPullRequest pullRequest = ghRepository.createPullRequest(
                "Feedback PR for " + repositoryName,
                "feedback",
                "main",
                "Please review the feedback provided."
        );

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

  public List<GHIssueComment> getReviews(String repositoryName){
    try {
      GHRepository repository = gitHub.getRepository(repositoryName);
      GHPullRequest pullRequest = repository.getPullRequest(1);
      return pullRequest.listComments().toList();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public GHIssueComment createReview(String content, String gitHubToken, String repositoryName) {
    try {
      return new GitHubBuilder().withOAuthToken(gitHubToken).build().getRepository(repositoryName).getPullRequest(1).comment(content);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }  }
}
