package com.example.classroom.dto.assignment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentToUpdateDTO {
  @NotNull(message = "Id must not be null")
  private Long id;
  private String name;
  private String description;
}