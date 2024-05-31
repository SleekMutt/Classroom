package com.example.classroom.service.github;

import com.example.classroom.entities.User;

import java.io.IOException;

public interface IGitHubService {
  String createRepository(String assignmentName, User mentor, User student) throws IOException;
}
