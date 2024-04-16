package com.example.classroom.service.assignment;

import com.example.classroom.dto.assignment.AssignmentDTO;
import com.example.classroom.dto.assignment.AssignmentToCreateDTO;
import com.example.classroom.dto.assignment.AssignmentToUpdateDTO;
import com.example.classroom.entities.Assignment;
import com.example.classroom.entities.AssignmentStudent;
import com.example.classroom.entities.AssignmentStudentId;
import com.example.classroom.entities.User;
import com.example.classroom.mapper.AssignmentMapper;
import com.example.classroom.repository.AssignmentRepository;
import com.example.classroom.repository.AssignmentStudentRepository;
import com.example.classroom.service.github.GitHubServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class AssignmentServiceImpl implements IAssignmentService{
  @Autowired
  private AssignmentRepository assignmentRepository;
  @Autowired
  private AssignmentStudentRepository assignmentStudentRepository;
  @Autowired
  private GitHubServiceImpl gitHubService;
  @Autowired
  private AssignmentMapper assignmentMapper;
  @Override
  public AssignmentDTO createAssignment(AssignmentToCreateDTO assignment) {
    return assignmentMapper.entityToDto(assignmentRepository.save(assignmentMapper.dtoToEntity(assignment)));
  }

  @Override
  public void deleteAssignment(Long id) {
    assignmentRepository.deleteById(id);
  }

  @Override
  public AssignmentDTO getAssignmentById(Long id) {
    return assignmentRepository.findById(id).map(assignmentMapper::entityToDto).orElseThrow(() -> new NoSuchElementException("No assignment was found"));
  }

  @Override
  public List<AssignmentDTO> getAllAssignment() {
    return assignmentRepository.findAll().stream().map(assignmentMapper::entityToDto).collect(Collectors.toList());
  }

  @Override
  public AssignmentDTO updateAssignment(AssignmentToUpdateDTO assignment) {
    Assignment assignment1 = assignmentRepository.findById(assignment.getId()).orElseThrow(() -> new NoSuchElementException("No course was found"));
    return assignmentMapper.entityToDto(assignmentRepository.save(assignmentMapper.dtoToEntity(assignment1, assignment)));
  }

  @Override
  public Assignment acceptAssigment(Long assignmentId, User user) {
    Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(NoSuchElementException::new);
    AssignmentStudent build = AssignmentStudent.builder().id(new AssignmentStudentId(user.getId(), assignmentId))
            .assignment(assignment)
            .user(user)
            .build();
    String repositoryName = gitHubService.createRepository(assignment.getCourse().getName() + "-" + assignment.getName()
            , assignment.getCourse().getOwner(), user);
    build.setRepositoryName(repositoryName);
    assignmentStudentRepository.save(build);
    return assignmentRepository.findById(assignmentId).orElseThrow(NoSuchElementException::new);
  }
}
