package com.example.classroom.dto.assignment;


import com.example.classroom.dto.course.CourseToUpdateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentToCreateDTO {
  private String name;
  private String description;
  @NotNull(message = "Course can't be null")
  @Valid
  private CourseToUpdateDTO course;
}
