package com.example.classroom.repository;

import com.example.classroom.entities.AssignmentStudent;
import com.example.classroom.entities.AssignmentStudentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface AssignmentStudentRepository extends JpaRepository<AssignmentStudent, AssignmentStudentId> {
  Optional<AssignmentStudent> findAssignmentStudentByRepositoryName(String name);
}
