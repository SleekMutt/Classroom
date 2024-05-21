package com.example.classroom.service.assignment;

import com.example.classroom.dto.assignment.AssignmentDTO;
import com.example.classroom.dto.assignment.AssignmentStudentDTO;
import com.example.classroom.dto.assignment.AssignmentToCreateDTO;
import com.example.classroom.dto.assignment.AssignmentToUpdateDTO;
import com.example.classroom.dto.comment.CommentDTO;
import com.example.classroom.entities.Assignment;
import com.example.classroom.entities.AssignmentStudent;
import com.example.classroom.entities.AssignmentStudentId;
import com.example.classroom.entities.TaskStatus;
import com.example.classroom.entities.User;
import com.example.classroom.mapper.AssignmentMapper;
import com.example.classroom.mapper.AssignmentStudentMapper;
import com.example.classroom.mapper.CommentMapper;
import com.example.classroom.repository.AssignmentRepository;
import com.example.classroom.repository.AssignmentStudentRepository;
import com.example.classroom.service.github.GitHubServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
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
  @Autowired
  private AssignmentStudentMapper assignmentStudentMapper;
  @Autowired
  private CommentMapper commentMapper;
  @Override
  public AssignmentDTO createAssignment(AssignmentToCreateDTO assignment) {
    Assignment assignment1 = assignmentMapper.dtoToEntity(assignment);
    assignment1.setCreatedAt(LocalDateTime.now());
    return assignmentMapper.entityToDto(assignmentRepository.save(assignment1));
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
            .status(TaskStatus.NOT_READY)
            .build();
    String repositoryName = gitHubService.createRepository(UUID.randomUUID().toString().replace("-", "")
            , assignment.getCourse().getOwner(), user);
    build.setRepositoryName(repositoryName);
    assignmentStudentRepository.save(build);
    return assignmentRepository.findById(assignmentId).orElseThrow(NoSuchElementException::new);
  }

  public Page<AssignmentDTO> getAssignmentsByCourseId(Long courseId, int page) {
    Page<Assignment> allByCourseId = assignmentRepository.findAllByCourse_Id(courseId, PageRequest.of(page, 5));
    return allByCourseId.map(assignment -> assignmentMapper.entityToDto(assignment));
  }
  public AssignmentStudentDTO getAcceptedAssignmentsById(Long assignmentId, Long userId) {
    return assignmentStudentMapper.entityToDto(assignmentStudentRepository.findById(new AssignmentStudentId(userId, assignmentId)).orElseThrow(() -> new NoSuchElementException("User hasn't accepted that assignment")));
  }
  public List<CommentDTO> getReviews(String repository) {
      return gitHubService.getReviews(repository).stream().map(elem -> {
        try {
          return commentMapper.entityToDto(elem);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }).collect(Collectors.toList());
  }
  public CommentDTO createReview(String content, String gitHubToken, String repositoryName) {
    try {
      return commentMapper.entityToDto(gitHubService.createReview(content, gitHubToken, repositoryName));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
