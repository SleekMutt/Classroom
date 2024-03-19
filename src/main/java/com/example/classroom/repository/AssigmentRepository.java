package com.example.classroom.repository;

import com.example.classroom.entities.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssigmentRepository extends JpaRepository<Assignment, Long> {
}
