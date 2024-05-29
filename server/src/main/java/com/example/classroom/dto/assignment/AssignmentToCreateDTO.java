package com.example.classroom.dto.assignment;


import com.example.classroom.dto.course.CourseToUpdateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentToCreateDTO {
  private String name;
  private String description;
  @NotNull(message = "Course can't be null")
  @Valid
  private CourseToUpdateDTO course;
  @Future(message = " must be in the future")
  @NotNull(message = " can't be null")
  private LocalDateTime deadline;
  @Positive(message = " must be positive")
  @NotNull(message = " can't be null")
  private Double rating;
}
