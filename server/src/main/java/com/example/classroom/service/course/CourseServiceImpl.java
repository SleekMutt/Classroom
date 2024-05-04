package com.example.classroom.service.course;

import com.example.classroom.dto.assignment.AssignmentToUpdateDTO;
import com.example.classroom.dto.course.CourseDTO;
import com.example.classroom.dto.course.CourseToCreateDTO;
import com.example.classroom.dto.course.CourseToUpdateDTO;
import com.example.classroom.entities.Course;
import com.example.classroom.entities.User;
import com.example.classroom.mapper.CourseMapper;
import com.example.classroom.repository.AssignmentRepository;
import com.example.classroom.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements ICourseService {
  @Autowired
  private CourseRepository courseRepository;
  @Autowired
  private CourseMapper courseMapper;
  @Autowired
  private AssignmentRepository assignmentRepository;

  @Override
  public CourseDTO createCourse(CourseToCreateDTO course, User user) {
    Course entity = courseMapper.dtoToEntity(course);
    entity.setOwner(user);
    entity.setJoiningCode(UUID.randomUUID().toString().replace("-", ""));
    return courseMapper.entityToDto(courseRepository.save(entity));
  }

  @Override
  public void deleteCourse(Long id) {
    courseRepository.deleteById(id);
  }

  @Override
  public CourseDTO getCourseById(Long id) {
    return courseRepository.findById(id).map(courseMapper::entityToDto).orElseThrow(() -> new NoSuchElementException("No course was found"));
  }
  @Override
  public List<CourseDTO> getAllCourses() {
    return courseRepository.findAll().stream().map(courseMapper::entityToDto).collect(Collectors.toList());
  }

  @Override
  public List<CourseDTO> getAllCoursesByOwner(Long id) {
    return courseRepository.findAllByOwner_Id(id).stream().map(courseMapper::entityToDto).collect(Collectors.toList());
  }

  @Override
  public List<CourseDTO> getAllJoinedByUser(Long id) {
    return courseRepository.findAllByStudentId(id).stream().map(courseMapper::entityToDto).collect(Collectors.toList());
  }

  @Override
  public CourseDTO updateCourse(CourseToUpdateDTO course) {
    Course course1 = courseRepository.findById(course.getId()).orElseThrow(() -> new NoSuchElementException("No course was found"));
    return courseMapper.entityToDto(courseRepository.save(courseMapper.dtoToEntity(course1, course)));
  }

  @Override
  public CourseDTO addStudentToTheCourseByCode(String code, User user) {
    Course course = courseRepository.findByJoiningCode(code).orElseThrow(() -> new NoSuchElementException("No course was found by that code"));
    List<User> students = course.getStudents();
    students.add(user);
    course.setStudents(students);
    courseRepository.save(course);
    return courseMapper.entityToDto(course);
  }
  //TODO делітати без eager бажано
  @Override
  public boolean isOwner(Long id, User user) {
    return user.getOwnedCourses().stream().anyMatch(course -> course.getId().equals(id));
  }
  @Override
  public boolean isOwner(AssignmentToUpdateDTO assignment, User user) {
    Course course1 = assignmentRepository.findById(assignment.getId()).orElseThrow(() -> new NoSuchElementException("No course was found by that code")).getCourse();
    return user.getOwnedCourses().stream().anyMatch(course -> course.getId().equals(course1.getId()));
  }
  @Override
  public boolean isOwnerByAssignmentId(Long assignmentId, User user) {
    Course course1 = assignmentRepository.findById(assignmentId).orElseThrow(() -> new NoSuchElementException("No course was found by that code")).getCourse();
    return user.getOwnedCourses().stream().anyMatch(course -> course.getId().equals(course1.getId()));
  }
  @Override
  public CourseDTO deleteUserFromTheCourse(Long courseId, Long userId) {
    Course course = courseRepository.findById(courseId).orElseThrow(() -> new NoSuchElementException("No course was found."));
    course.setStudents(course.getStudents().stream().filter(student -> !student.getId().equals(userId)).collect(Collectors.toList()));
    courseRepository.save(course);
    return courseMapper.entityToDto(course);
  }
}
