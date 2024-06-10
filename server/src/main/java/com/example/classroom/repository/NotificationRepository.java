package com.example.classroom.repository;

import com.example.classroom.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
  List<Notification> getAllByUser_GitHubUsername(String username);
  List<Notification> getAllByUser_Id(Long id);
}
