package com.example.classroom.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AssignmentStudentId implements Serializable {
  @Column(name = "user_id")
  private Long userId;
  @Column(name = "assignment_id")
  private Long assigmentId;
}