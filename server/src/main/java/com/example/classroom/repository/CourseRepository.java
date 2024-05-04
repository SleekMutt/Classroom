package com.example.classroom.repository;

import com.example.classroom.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
  Optional<Course> findByJoiningCode(String code);
  List<Course> findAllByOwner_Id(Long id);
  @Query("SELECT c FROM Course c JOIN c.students s WHERE s.id = :studentId")
  List<Course> findAllByStudentId(@Param("studentId") Long studentId);
}
