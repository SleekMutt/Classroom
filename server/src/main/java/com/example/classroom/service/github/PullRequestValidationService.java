package com.example.classroom.service.github;

import com.example.classroom.dto.request.OpenAiValidationResponseDto;
import com.example.classroom.dto.request.PullRequestPatchDTO;
import com.example.classroom.dto.request.QueuedValidationProcessDto;
import com.example.classroom.service.openai.OpenAiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestReviewBuilder;
import org.kohsuke.github.GHPullRequestReviewEvent;
import org.kohsuke.github.GHRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

@Service
public class PullRequestValidationService {

  private final GHOrganization ghOrganization;
  private final ObjectMapper objectMapper;
  private final OpenAiService openAiService;
  private final Queue<QueuedValidationProcessDto> requests = new LinkedList<>();
  private static final String PROMPT_TEMPLATE = "Hi ChatGPT, could you please review my homework " +
          "and provide detailed comments on it? " +
          "I'm looking for feedback on the content, structure, clarity, " +
          "and any areas where I can improve. "
          + "Please return the feedback in the following format:"
          + "{"
          + "  \"rating\": ${rating_integer},"
          + "  \"files\": ["
          + "    {"
          + "      \"filename\": \"${filename}\","
          + "      \"comments\": ["
          + "        {"
          + "          \"line\": ${line_number},"
          + "          \"comment\": \"${comment}\""
          + "        }"
          + "      ]"
          + "    }"
          + "  ]"
          + "}"
          + "The homework is provided in json format [{filename: ${filename}, code: ${code}}]. "
          + "Here is my homework is: %s";


  public PullRequestValidationService(GHOrganization ghOrganization,
                                      ObjectMapper objectMapper, OpenAiService openAiService) {
    this.ghOrganization = ghOrganization;
    this.objectMapper = objectMapper;
    this.openAiService = openAiService;
  }

  public void validatePullRequest(String repositoryName) {
    try {
      GHRepository repository = ghOrganization.getRepository(repositoryName);
      GHPullRequest pullRequest = repository.getPullRequest(1);

      List<PullRequestPatchDTO> fileList = new ArrayList<>();
      pullRequest.listFiles().forEach(
              file -> {
                if (!file.getFilename().equals("DUMMY.md")) {
                  fileList.add(
                          new PullRequestPatchDTO(file.getFilename(), file.getPatch()));
                }}
      );

      String prompt = String.format(PROMPT_TEMPLATE, objectMapper.writeValueAsString(fileList));
      requests.add(QueuedValidationProcessDto.builder()
              .prompt(prompt)
              .pullRequest(pullRequest)
              .fileCodeLines(fileList.stream()
                      .collect(
                              Collectors.toMap(
                                      PullRequestPatchDTO::getFilename,
                                      pullrequestPatchDto ->
                                              pullrequestPatchDto.getCode().split("\n").length
                              )
                      )
              )
              .build());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Scheduled(fixedDelay = 21000)
  private void processValidation() {
    if (!requests.isEmpty()) {
      QueuedValidationProcessDto process = requests.poll();
      String prompt = process.getPrompt();
      GHPullRequest pullRequest = process.getPullRequest();
      GHPullRequestReviewBuilder review = pullRequest.createReview();
      try {
        String response = openAiService.processPrompt(prompt);
        OpenAiValidationResponseDto openAiResponse = objectMapper.readValue(response,
                OpenAiValidationResponseDto.class);
        try {
          openAiResponse.getFiles().forEach(file -> {
            int codeLines = process.getFileCodeLines().get(file.getFilename());
            file.getComments().forEach(comment -> {
              if (codeLines > comment.getLine()) {
                review.comment(comment.getComment(), file.getFilename(), comment.getLine());
              }
            });
          });
          GHPullRequestReviewEvent taskStatus = GHPullRequestReviewEvent.COMMENT;

          review.event(taskStatus);
          review.body("OpenAI previous review of the pullrequest. "
                  + "The rating is "
                  + openAiResponse.getRating() + "/10.");
          review.create();

        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      } catch (JsonProcessingException exception) {
        requests.add(process);
      }
    }
  }
}