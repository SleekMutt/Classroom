package com.example.classroom.dto.course;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseToUpdateDTO {
  @NotNull(message = "Course id cannot be null")
  private Long id;
  private String name;
}
