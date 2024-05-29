package com.example.classroom.dto.assignment;

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
public class AssignmentToUpdateDTO {
  @NotNull(message = " must not be null")
  private Long id;
  private String name;
  private String description;
  @Future(message = " must be in the future")
  @NotNull(message = " can't be null")
  private LocalDateTime deadline;
  @Positive(message = " must be positive")
  @NotNull(message = "can't be null")
  private Double rating;
}