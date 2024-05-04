package com.example.classroom.controllers;

import com.example.classroom.dto.course.CourseDTO;
import com.example.classroom.dto.course.CourseToCreateDTO;
import com.example.classroom.dto.course.CourseToUpdateDTO;
import com.example.classroom.entities.User;
import com.example.classroom.service.course.CourseServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {
  @Autowired
  private CourseServiceImpl courseService;
  @GetMapping("/")
  public ResponseEntity<List<CourseDTO>> getAllCourse()  {
    return new ResponseEntity<>(courseService.getAllCourses(), HttpStatus.OK);
  }
  @GetMapping("/owned-courses")
  public ResponseEntity<List<CourseDTO>> getAllOwnedCourses(@AuthenticationPrincipal User user)  {
    return new ResponseEntity<>(courseService.getAllCoursesByOwner(user.getId()), HttpStatus.OK);
  }
  @GetMapping("/joined-courses")
  public ResponseEntity<List<CourseDTO>> getAllJoinedCourses(@AuthenticationPrincipal User user)  {
    return new ResponseEntity<>(courseService.getAllJoinedByUser(user.getId()), HttpStatus.OK);
  }
  @GetMapping("/{id}")
  public ResponseEntity<CourseDTO> getCourseById(@PathVariable("id") Long id)  {
    return new ResponseEntity<>(courseService.getCourseById(id), HttpStatus.OK);
  }
  @PostMapping("/")
  public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseToCreateDTO course, @AuthenticationPrincipal User user)  {
    return new ResponseEntity<>(courseService.createCourse(course, user), HttpStatus.OK);
  }
  @PreAuthorize("@courseServiceImpl.isOwner(#id, principal) or hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> deleteCourse(@PathVariable("id") Long id)  {
    courseService.deleteCourse(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
  @PreAuthorize("@courseServiceImpl.isOwner(#course.id, principal) or hasRole('ADMIN')")
  @PutMapping("/")
  public ResponseEntity<CourseDTO> updateCourse(@RequestBody @Valid CourseToUpdateDTO course)  {
    return new ResponseEntity<>(courseService.updateCourse(course), HttpStatus.OK);
  }
  @PatchMapping("/join-course")
  public ResponseEntity<CourseDTO> joinToTheCourse(@RequestParam String code, @AuthenticationPrincipal User user)  {
    return new ResponseEntity<>(courseService.addStudentToTheCourseByCode(code, user), HttpStatus.OK);
  }
  @PatchMapping("/leave-course")
  public ResponseEntity<CourseDTO> leaveTheCourse(@RequestParam Long id, @AuthenticationPrincipal User user)  {
    return new ResponseEntity<>(courseService.deleteUserFromTheCourse(id, user.getId()), HttpStatus.OK);
  }
  @PreAuthorize("@courseServiceImpl.isOwner(#courseId, principal) or hasRole('ADMIN')")
  @PatchMapping("/delete-user-from-course")
  public ResponseEntity<CourseDTO> deleteUserFromCourse(@RequestParam Long courseId, @RequestParam Long userId)  {
    return new ResponseEntity<>(courseService.deleteUserFromTheCourse(courseId, userId), HttpStatus.OK);
  }

}
