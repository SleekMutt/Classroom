package com.example.classroom.entities.listener;

import com.example.classroom.entities.Assignment;
import com.example.classroom.entities.AssignmentStudent;
import jakarta.persistence.PreRemove;
import org.kohsuke.github.GHOrganization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class AssignmentListener {
  @Autowired
  private GHOrganization organization;
  @PreRemove
  public void preRemove(Assignment assignment) {
    List<AssignmentStudent> studentsAcceptedTask = assignment.getStudentsAcceptedTask();
    studentsAcceptedTask.forEach(student -> {
      try {
        organization.getRepository(student.getRepositoryName()).delete();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }
}
