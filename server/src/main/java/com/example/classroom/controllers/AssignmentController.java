package com.example.classroom.controllers;


import com.example.classroom.dto.assignment.AssignmentDTO;
import com.example.classroom.dto.assignment.AssignmentStudentDTO;
import com.example.classroom.dto.assignment.AssignmentToCreateDTO;
import com.example.classroom.dto.assignment.AssignmentToUpdateDTO;
import com.example.classroom.dto.comment.CommentRequest;
import com.example.classroom.entities.User;
import com.example.classroom.service.assignment.AssignmentServiceImpl;
import jakarta.validation.Valid;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
  @GetMapping("/page-by-course-id")
  public ResponseEntity<Page<AssignmentDTO>> getAssignmentsByCourseId(@RequestParam Long courseId, @RequestParam int page)  {
    return new ResponseEntity<>(assignmentService.getAssignmentsByCourseId(courseId, page), HttpStatus.OK);
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
  @PostMapping("/add-files-to-repository")
  public ResponseEntity<?> getFile(@RequestParam("repositoryName") String repositoryName, @RequestParam("files") List<MultipartFile> files,
                                   @AuthenticationPrincipal User user) throws IOException {
    assignmentService.addFilesToRepository(repositoryName, user, files);
    return new ResponseEntity<>(null, HttpStatus.OK);
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
  @PutMapping("/accept-assignment/{id}")
  public ResponseEntity<HttpStatus> acceptAssignment(@PathVariable("id") Long id, @AuthenticationPrincipal User user)  {
    assignmentService.acceptAssigment(id, user);
    return new ResponseEntity<>(HttpStatus.OK);
  }
  @PutMapping("/rate-assignment")
  public ResponseEntity<AssignmentStudentDTO> rateAssignment(@RequestParam("assignmentId") Long assignmentId, @RequestParam("userId") Long userId
          ,@RequestParam("rating") Double rating)  {
    return new ResponseEntity<>(assignmentService.rateAssignment(assignmentId, userId, rating), HttpStatus.OK);
  }
  @GetMapping("/accepted-assignment/{id}")
  public ResponseEntity<AssignmentStudentDTO> getAcceptedAssignment(@PathVariable("id") Long id, @AuthenticationPrincipal User user)  {
      return new ResponseEntity<>(assignmentService.getAcceptedAssignmentsById(id, user.getId()), HttpStatus.OK);
  }
  @GetMapping("/accepted-assignment")
  public ResponseEntity<AssignmentStudentDTO> getAcceptedAssignment(@RequestParam("userId") Long id, @RequestParam("courseId") Long courseId)  {
      return new ResponseEntity<>(assignmentService.getAcceptedAssignmentsById(courseId, id), HttpStatus.OK);
  }
  @GetMapping("/reviews")
  public ResponseEntity<?> getComment(@RequestParam("repositoryName") String repositoryName)  {
      return new ResponseEntity<>(assignmentService.getReviews(repositoryName), HttpStatus.OK);
  }
  @PutMapping("/reviews")
  public ResponseEntity<?> updateComment(@RequestBody CommentRequest comment, @AuthenticationPrincipal User user)  {
      return new ResponseEntity<>(assignmentService.updateReview(comment.getBody(), user.getGitHubToken(), comment.getRepositoryName(), comment.getId()), HttpStatus.OK);
  }
  @PostMapping("/reviews")
  public ResponseEntity<?> createComment(@RequestBody CommentRequest comment, @AuthenticationPrincipal User user)  {
      return new ResponseEntity<>(assignmentService.createReview(comment.getBody(), user.getGitHubToken(), comment.getRepositoryName()), HttpStatus.OK);
  }
  @DeleteMapping("/reviews")
  public ResponseEntity<?> deleteComment(@RequestParam("id") Long id, @RequestParam("repositoryName") String repositoryName, @AuthenticationPrincipal User user)  {
    assignmentService.deleteComment(user, repositoryName, id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
