package com.example.classroom.dto.course;

import com.example.classroom.dto.assignment.AssignmentDTO;
import com.example.classroom.dto.user.CourseParticipantDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseDTO {
  private Long id;
  private String name;
  private String description;
  private String joiningCode;
  private CourseParticipantDTO owner;
  private List<CourseParticipantDTO> students;
  private List<AssignmentDTO> assignments;
}
