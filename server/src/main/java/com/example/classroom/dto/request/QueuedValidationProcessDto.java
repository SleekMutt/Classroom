package com.example.classroom.dto.request;
import java.util.Map;

import com.example.classroom.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kohsuke.github.GHPullRequest;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueuedValidationProcessDto {
  private String prompt;
  private User user;
  private GHPullRequest pullRequest;
  private Map<String, Integer> fileCodeLines;
}