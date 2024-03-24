package com.example.classroom.service.assignment;

import com.example.classroom.entities.Assignment;
import com.example.classroom.entities.User;

import java.util.List;

public interface IAssignmentService {
  Assignment createAssignment(Assignment assignment);
  void deleteAssignment(Long id);
  Assignment getAssignmentById(Long id);
  List<Assignment> getAllAssignment();
  Assignment updateAssignment(Assignment assignment);

  Assignment acceptAssigment(Long assignmentId, User user);
}
