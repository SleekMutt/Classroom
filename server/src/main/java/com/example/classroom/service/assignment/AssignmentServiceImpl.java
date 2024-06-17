package com.example.classroom.service.assignment;

import com.example.classroom.dto.assignment.AssignmentDTO;
import com.example.classroom.dto.assignment.AssignmentStudentDTO;
import com.example.classroom.dto.assignment.AssignmentToCreateDTO;
import com.example.classroom.dto.assignment.AssignmentToUpdateDTO;
import com.example.classroom.dto.comment.CommentDTO;
import com.example.classroom.entities.Assignment;
import com.example.classroom.entities.AssignmentStudent;
import com.example.classroom.entities.AssignmentStudentId;
import com.example.classroom.entities.Notification;
import com.example.classroom.entities.TaskStatus;
import com.example.classroom.entities.User;
import com.example.classroom.mapper.AssignmentMapper;
import com.example.classroom.mapper.AssignmentStudentMapper;
import com.example.classroom.mapper.CommentMapper;
import com.example.classroom.repository.AssignmentRepository;
import com.example.classroom.repository.AssignmentStudentRepository;
import com.example.classroom.service.github.GitHubServiceImpl;
import com.example.classroom.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
  @Autowired
  private NotificationService notificationService;
  @Override
  public AssignmentDTO createAssignment(AssignmentToCreateDTO assignment, List<MultipartFile> files) throws IOException {
    Assignment assignment1 = assignmentMapper.dtoToEntity(assignment);
    assignment1.setCreatedAt(LocalDateTime.now());
    String repositoryName = UUID.randomUUID().toString().replace("-", "");
    assignment1.setBaseRepositoryName(gitHubService.createBaseRepository(repositoryName, files));
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
  public Assignment acceptAssigment(Long assignmentId, User user) throws IOException {
    Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(NoSuchElementException::new);
    AssignmentStudent build = AssignmentStudent.builder().id(new AssignmentStudentId(user.getId(), assignmentId))
            .assignment(assignment)
            .user(user)
            .status(TaskStatus.NOT_READY)
            .build();
    String repositoryName = gitHubService.createRepository(assignment.getBaseRepositoryName()
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
  public CommentDTO updateReview(String content, String gitHubToken, String repositoryName, Long id) {
    try {
      return commentMapper.entityToDto(gitHubService.updateReview(content, gitHubToken, repositoryName, id));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void deleteComment(User user, String repositoryName, Long id) {
    gitHubService.deleteComment(user.getGitHubToken(), repositoryName, id);
  }

  public AssignmentStudentDTO rateAssignment(Long assignmentId, Long userId, Double rating) {
    AssignmentStudent assignmentStudent = assignmentStudentRepository.findById(new AssignmentStudentId(userId, assignmentId))
            .orElseThrow(() -> new NoSuchElementException("User hasn't accepted that assignment"));
    assignmentStudent.setRating(rating);
    AssignmentStudent result = assignmentStudentRepository.save(assignmentStudent);
    notificationService.sendNotification("Your assignment " + result.getAssignment().getName() + " was rated. Please review it", result.getUser());
    return assignmentStudentMapper.entityToDto(result);
  }
  public void addFilesToRepository(String repositoryName, User user, List<MultipartFile> files) throws IOException {
    AssignmentStudent test = assignmentStudentRepository.findAssignmentStudentByRepositoryName(repositoryName.substring(repositoryName.lastIndexOf('/') + 1))
            .orElseThrow(() -> new NoSuchElementException("No accepted assignment  was found"));
    test.setStatus(TaskStatus.READY);
    assignmentStudentRepository.save(test);
    gitHubService.addFilesToRepository(repositoryName, user, files);
  }
}
