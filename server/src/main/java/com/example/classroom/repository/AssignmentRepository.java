package com.example.classroom.repository;

import com.example.classroom.entities.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
  Page<Assignment> findAllByCourse_Id(Long courseId, Pageable pageable);
}
