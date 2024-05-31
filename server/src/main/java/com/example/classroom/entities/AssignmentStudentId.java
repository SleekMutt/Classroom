package com.example.classroom.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class AssignmentStudentId implements Serializable {
  @Column(name = "user_id")
  private Long userId;
  @Column(name = "assignment_id")
  private Long assignmentId;
}