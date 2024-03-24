package com.example.classroom.service.assignment;

import com.example.classroom.entities.Assignment;
import com.example.classroom.entities.AssignmentStudent;
import com.example.classroom.entities.AssignmentStudentId;
import com.example.classroom.entities.User;
import com.example.classroom.repository.AssignmentRepository;
import com.example.classroom.repository.AssignmentStudentRepository;
import com.example.classroom.service.github.GitHubServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AssignmentServiceImpl implements IAssignmentService{
  @Autowired
  private AssignmentRepository assignmentRepository;
  @Autowired
  private AssignmentStudentRepository assignmentStudentRepository;
  @Autowired
  private GitHubServiceImpl gitHubService;
  @Override
  public Assignment createAssignment(Assignment assignment) {
    return assignmentRepository.save(assignment);
  }

  @Override
  public void deleteAssignment(Long id) {
    assignmentRepository.deleteById(id);
  }

  @Override
  public Assignment getAssignmentById(Long id) {
    return assignmentRepository.findById(id).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public List<Assignment> getAllAssignment() {
    return assignmentRepository.findAll();
  }

  @Override
  public Assignment updateAssignment(Assignment assignment) {
    return assignmentRepository.save(assignment);
  }

  @Override
  public Assignment acceptAssigment(Long assignmentId, User user) {
    Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(NoSuchElementException::new);
    AssignmentStudent build = AssignmentStudent.builder().id(new AssignmentStudentId(user.getId(), assignmentId))
            .assignment(assignment)
            .user(user)
            .build();
    String repositoryName = gitHubService.createRepository(assignment.getName(), assignment.getCourse().getOwner(), user);
    build.setRepositoryName(repositoryName);
    System.out.println(assignmentStudentRepository.save(build).getId());
    return assignmentRepository.findById(assignmentId).orElseThrow(NoSuchElementException::new);
  }
}
