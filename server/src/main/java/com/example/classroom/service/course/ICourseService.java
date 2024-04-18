package com.example.classroom.service.course;


import com.example.classroom.dto.assignment.AssignmentToUpdateDTO;
import com.example.classroom.dto.course.CourseDTO;
import com.example.classroom.dto.course.CourseToCreateDTO;
import com.example.classroom.dto.course.CourseToUpdateDTO;
import com.example.classroom.entities.User;

import java.util.List;

public interface ICourseService {
  CourseDTO createCourse(CourseToCreateDTO course, User user);
  void deleteCourse(Long id);
  CourseDTO getCourseById(Long id);
  List<CourseDTO> getAllCourses();
  CourseDTO updateCourse(CourseToUpdateDTO course);
  CourseDTO addStudentToTheCourseByCode(String code, User user);
  boolean isOwner(Long id, User user);
  boolean isOwner(AssignmentToUpdateDTO assignment, User user);

  boolean isOwnerByAssignmentId(Long assignmentId, User user);

  CourseDTO deleteUserFromTheCourse(Long courseId, Long userId);
}
