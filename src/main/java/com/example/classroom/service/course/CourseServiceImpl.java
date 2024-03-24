package com.example.classroom.service.course;

import com.example.classroom.entities.Course;
import com.example.classroom.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CourseServiceImpl implements ICourseService {
  @Autowired
  private CourseRepository courseRepository;

  @Override
  public Course createCourse(Course course) {
    return courseRepository.save(course);
  }

  @Override
  public void deleteCourse(Long id) {
    courseRepository.deleteById(id);
  }

  @Override
  public Course getCourseById(Long id) {
    return courseRepository.findById(id).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public List<Course> getAllCourses() {
    return courseRepository.findAll();
  }

  @Override
  public Course updateCourse(Course course) {
    return courseRepository.save(course);
  }
}
