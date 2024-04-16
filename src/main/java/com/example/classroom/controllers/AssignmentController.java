package com.example.classroom.controllers;


import com.example.classroom.dto.assignment.AssignmentDTO;
import com.example.classroom.dto.assignment.AssignmentToCreateDTO;
import com.example.classroom.dto.assignment.AssignmentToUpdateDTO;
import com.example.classroom.entities.User;
import com.example.classroom.service.assignment.AssignmentServiceImpl;
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
@RequestMapping("/assignment")
public class AssignmentController {
  @Autowired
  private AssignmentServiceImpl assignmentService;
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/")
  public ResponseEntity<List<AssignmentDTO>> getAllAssignments()  {
    return new ResponseEntity<>(assignmentService.getAllAssignment(), HttpStatus.OK);
  }
  @GetMapping("/{id}")
  public ResponseEntity<AssignmentDTO> getAssignmentById(@PathVariable("id") Long id)  {
    return new ResponseEntity<>(assignmentService.getAssignmentById(id), HttpStatus.OK);
  }
  @PreAuthorize("@courseServiceImpl.isOwner(#assignment.course.id, principal) or hasRole('ADMIN')")
  @PostMapping("/")
  public ResponseEntity<AssignmentDTO> createAssignment(@RequestBody @Valid AssignmentToCreateDTO assignment)  {
    return new ResponseEntity<>(assignmentService.createAssignment(assignment), HttpStatus.OK);
  }
  @PreAuthorize("@courseServiceImpl.isOwner(#assignment, principal)")
  @PutMapping("/")
  public ResponseEntity<AssignmentDTO> updateAssignment(@RequestBody AssignmentToUpdateDTO assignment)  {
    return new ResponseEntity<>(assignmentService.updateAssignment(assignment), HttpStatus.OK);
  }
  @PreAuthorize("@courseServiceImpl.isOwnerByAssignmentId(#id, principal) or hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> deleteAssignment(@PathVariable("id") Long id)  {
    assignmentService.deleteAssignment(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
  @PatchMapping("/accept-assignment")
  public ResponseEntity<HttpStatus> acceptAssignment(@RequestParam("id") Long id, @AuthenticationPrincipal User user)  {
    assignmentService.acceptAssigment(id, user);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
