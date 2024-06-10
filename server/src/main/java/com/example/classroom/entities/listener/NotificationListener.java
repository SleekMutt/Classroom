package com.example.classroom.entities.listener;

import com.example.classroom.entities.Notification;
import com.example.classroom.service.notification.NotificationService;
import com.example.classroom.service.user.UserServiceImpl;
import jakarta.persistence.PostPersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;


@Component
public class NotificationListener {
  @Autowired
  @Lazy
  private SimpMessagingTemplate messagingTemplate;

  @PostPersist
  public void postPersists(Notification notification) {
    messagingTemplate.convertAndSendToUser(notification.getUser().getGitHubUsername(), "/topic/notifications",
            notification);
  }
}