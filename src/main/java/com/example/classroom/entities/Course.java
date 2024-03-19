package com.example.classroom.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Course {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  @ManyToOne(optional = false)
  @JoinColumn(name = "owner_id")
  private User owner;
  @ManyToMany
  @JoinTable(name = "course_student",
          joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"),
          inverseJoinColumns = @JoinColumn(name = "student_id",
                  referencedColumnName = "id"))
  private List<User> students;
  @OneToMany(mappedBy = "course")
  private List<Assignment> assignments;
}