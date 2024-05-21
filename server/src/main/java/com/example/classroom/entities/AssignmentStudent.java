package com.example.classroom.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
public class AssignmentStudent {
  @EmbeddedId
  private AssignmentStudentId id;
  @ManyToOne
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  private User user;
  @ManyToOne
  @MapsId("assignmentId")
  @JoinColumn(name = "assignment_id")
  private Assignment assignment;
  private String repositoryName;
  private Long rating;
  @Enumerated(EnumType.ORDINAL)
  private TaskStatus status;
}