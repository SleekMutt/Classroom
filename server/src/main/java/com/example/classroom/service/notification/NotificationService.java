package com.example.classroom.service.notification;

import com.example.classroom.dto.notification.NotificationDTO;
import com.example.classroom.entities.Notification;
import com.example.classroom.entities.User;
import com.example.classroom.mapper.NotificationMapper;
import com.example.classroom.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
  @Autowired
  private NotificationRepository notificationRepository;
  @Autowired
  private NotificationMapper notificationMapper;

  public List<NotificationDTO> getNotificationsByUsername(String username){
    return notificationRepository.getAllByUser_GitHubUsername(username).stream().map(notificationMapper::entityToDto).collect(Collectors.toList());
  }
  public List<NotificationDTO> getNotificationsByUserId(Long id){
    return notificationRepository.getAllByUser_Id(id).stream().map(notificationMapper::entityToDto).collect(Collectors.toList());
  }
  public void sendNotification(String message, User user){
    notificationRepository.save(Notification.builder().sentDate(LocalDateTime.now()).user(user).message(message).build());
  }
}
