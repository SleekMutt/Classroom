package com.example.classroom.dto.user;

import com.example.classroom.dto.course.CourseDTO;
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
public class UserDTO {
  private Long id;
  private String role;
  private String gitHubUsername;
  private List<CourseDTO> ownedCourses;
  private List<CourseDTO> courses;
}
