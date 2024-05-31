package com.example.classroom.service.github;

import com.example.classroom.dto.comment.CommentDTO;
import com.example.classroom.entities.User;
import com.example.classroom.exception.UserOrganizationAbsenceException;
import org.kohsuke.github.GHBranchProtectionBuilder;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHContentUpdateResponse;
import org.kohsuke.github.GHCreateRepositoryBuilder;
import org.kohsuke.github.GHFileNotFoundException;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class GitHubServiceImpl implements IGitHubService{
  @Autowired
  private GitHub gitHub;
  @Autowired
  private GHOrganization organization;

  @Override
  public String createRepository(String baseRepositoryName, User mentor, User student) throws IOException {
    String repositoryName = UUID.randomUUID().toString()
            .replace("-", "") + "-" + student.getGitHubUsername();
    try {
      GHRepository ghRepository = organization
              .createRepository(repositoryName).owner("ClassroomSleek")
              .fromTemplateRepository("ClassroomSleek", baseRepositoryName).done();

      ghRepository.setPrivate(true);
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
        Thread.sleep(3000);
        ghRepository.createRef("refs/heads/feedback", ghRepository.getRefs()[0].getObject().getSha());

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
        return ghRepository.getName();
      }
      catch (UserOrganizationAbsenceException e){
        ghRepository.delete();
        throw e;
      }
      catch (IOException e) {
        //ghRepository.delete();
        throw new RuntimeException(e);
      } catch (InterruptedException e) {
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
      GitHub connection = new GitHubBuilder().withOAuthToken(gitHubToken).build();
      if(!organization.hasMember(connection.getMyself())){
        inviteUserToOrganization(connection.getMyself().getLogin());
        throw new UserOrganizationAbsenceException("User " + connection.getMyself().getLogin() + " is not part of the organization"
                + "Please accept invitation to be part of the organization");
      }
      return connection.getRepository(repositoryName).getPullRequest(1).comment(content);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }  }
  public GHIssueComment updateReview(String content, String gitHubToken, String repositoryName, Long id) {
    try {
      GitHub connection = new GitHubBuilder().withOAuthToken(gitHubToken).build();
      if(!organization.hasMember(connection.getMyself())){
        inviteUserToOrganization(connection.getMyself().getLogin());
        throw new UserOrganizationAbsenceException("User " + connection.getMyself().getLogin() + " is not part of the organization"
                + "Please accept invitation to be part of the organization");
      }
      GHIssueComment commentToUpdate = connection.getRepository(repositoryName).getPullRequest(1)
              .getComments().stream().filter(elem -> elem.getId() == id)
              .findFirst().orElseThrow(() -> new NoSuchElementException("No such comment was found"));
      commentToUpdate.update(content);
      return commentToUpdate;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }  }

  public void deleteComment(String token, String repositoryName, Long id) {
    try {
      GitHub connection = new GitHubBuilder().withOAuthToken(token).build();
      if(!organization.hasMember(connection.getMyself())){
        inviteUserToOrganization(connection.getMyself().getLogin());
        throw new UserOrganizationAbsenceException("User " + connection.getMyself().getLogin() + " is not part of the organization"
                + "Please accept invitation to be part of the organization");
      }
      connection.getRepository(repositoryName).getPullRequest(1)
              .getComments().stream().filter(elem -> elem.getId() == id)
              .findFirst().orElseThrow(() -> new NoSuchElementException("No such comment was found")).delete();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void addFilesToRepository(String repositoryName, User user, List<MultipartFile> files) throws IOException {
    GHRepository repository = gitHub.getRepository(repositoryName);

    for (MultipartFile file : files) {
      File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
      try (FileOutputStream fos = new FileOutputStream(convFile)) {
        fos.write(file.getBytes());
      }


      try {
        GHContent content = repository.getFileContent(file.getOriginalFilename(), "feedback");
        GHContentUpdateResponse response = content.update(file.getBytes(), "Updated file " + file.getOriginalFilename(), "feedback");
      } catch (GHFileNotFoundException e) {
        repository.createContent()
                .branch("feedback")
                .path(file.getOriginalFilename())
                .content(file.getBytes())
                .message("Add file " + file.getOriginalFilename())
                .commit();
      }

    }

  }

  public String createBaseRepository(String repositoryName, List<MultipartFile> files) throws IOException {
      GHCreateRepositoryBuilder repository = organization.createRepository(repositoryName);
      repository.isTemplate(true);
      repository.private_(true);
      GHRepository ghRepository = repository.create();
      ghRepository.createContent()
              .branch("main")
              .path("README.md")
              .content("Welcome to " + repositoryName + ".")
              .message("Initial commit - Adding README")
              .commit();

      for (MultipartFile file : files) {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
          fos.write(file.getBytes());
        }
        ghRepository.createContent()
                .path(file.getOriginalFilename())
                .content(file.getBytes())
                .message("Add file " + file.getOriginalFilename())
                .commit();

      }

      return repositoryName;
  }
}
