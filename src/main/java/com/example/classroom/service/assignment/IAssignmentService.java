package com.example.classroom.service.assignment;

import com.example.classroom.dto.assignment.AssignmentDTO;
import com.example.classroom.dto.assignment.AssignmentToCreateDTO;
import com.example.classroom.dto.assignment.AssignmentToUpdateDTO;
import com.example.classroom.entities.Assignment;
import com.example.classroom.entities.User;

import java.util.List;

public interface IAssignmentService {
  AssignmentDTO createAssignment(AssignmentToCreateDTO assignment);
  void deleteAssignment(Long id);
  AssignmentDTO getAssignmentById(Long id);
  List<AssignmentDTO> getAllAssignment();
  AssignmentDTO updateAssignment(AssignmentToUpdateDTO assignment);

  Assignment acceptAssigment(Long assignmentId, User user);
}
