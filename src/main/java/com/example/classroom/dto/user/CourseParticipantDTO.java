package com.example.classroom.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseParticipantDTO {
  private Long id;
  private String gitHubUsername;

}
