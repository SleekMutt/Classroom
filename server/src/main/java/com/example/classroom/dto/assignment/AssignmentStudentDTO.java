package com.example.classroom.dto.assignment;


import com.example.classroom.dto.user.CourseParticipantDTO;
import com.example.classroom.entities.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentStudentDTO {
  private CourseParticipantDTO user;
  private String repositoryName;
  private Long rating;
  private TaskStatus status;
}
