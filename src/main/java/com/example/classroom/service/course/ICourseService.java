package com.example.classroom.service.course;


import com.example.classroom.entities.Course;

import java.util.List;

public interface ICourseService {
  Course createCourse(Course course);
  void deleteCourse(Long id);
  Course getCourseById(Long id);
  List<Course> getAllCourses();
  Course updateCourse(Course course);
}
