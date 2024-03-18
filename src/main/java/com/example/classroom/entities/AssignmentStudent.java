package com.example.classroom.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class AssignmentStudent {
  @EmbeddedId
  private AssignmentStudentId id;
  @ManyToOne
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  private User user;
  @ManyToOne
  @MapsId("assigmentId")
  @JoinColumn(name = "assignment_id")
  private Assignment assignment;
  private String repositoryName;
}