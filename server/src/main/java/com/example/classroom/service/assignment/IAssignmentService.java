package com.example.classroom.service.assignment;

import com.example.classroom.dto.assignment.AssignmentDTO;
import com.example.classroom.dto.assignment.AssignmentToCreateDTO;
import com.example.classroom.dto.assignment.AssignmentToUpdateDTO;
import com.example.classroom.entities.Assignment;
import com.example.classroom.entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IAssignmentService {
  AssignmentDTO createAssignment(AssignmentToCreateDTO assignment, List<MultipartFile> files) throws IOException;
  void deleteAssignment(Long id);
  AssignmentDTO getAssignmentById(Long id);
  List<AssignmentDTO> getAllAssignment();
  AssignmentDTO updateAssignment(AssignmentToUpdateDTO assignment);

  Assignment acceptAssigment(Long assignmentId, User user) throws IOException;
}
