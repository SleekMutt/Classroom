package com.example.classroom.dto.assignment;


import com.example.classroom.dto.course.CourseToUpdateDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentDTO {
  private Long id;
  private String name;
  private String description;
  private List<AssignmentStudentDTO> studentsAcceptedTask;
  private CourseToUpdateDTO course;
}
