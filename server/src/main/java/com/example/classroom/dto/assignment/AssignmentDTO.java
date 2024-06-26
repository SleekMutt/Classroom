package com.example.classroom.dto.assignment;


import com.example.classroom.dto.course.CourseToUpdateDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentDTO {
  private Long id;
  private String name;
  private String description;
  private String baseRepositoryName;

  private LocalDateTime createdAt;
  private LocalDateTime deadline;
  private Double rating;
  private List<AssignmentStudentDTO> studentsAcceptedTask;
  private CourseToUpdateDTO course;
}
