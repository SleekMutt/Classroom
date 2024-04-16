package com.example.classroom.repository;

import com.example.classroom.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
  Optional<Course> findByJoiningCode(String code);
}
