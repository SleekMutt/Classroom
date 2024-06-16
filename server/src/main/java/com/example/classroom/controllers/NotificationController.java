package com.example.classroom.controllers;

  import com.example.classroom.dto.notification.NotificationDTO;
  import com.example.classroom.service.notification.NotificationService;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.messaging.handler.annotation.Payload;
  import org.springframework.messaging.simp.annotation.SubscribeMapping;
  import org.springframework.stereotype.Controller;

  import java.security.Principal;
  import java.util.List;


  @Controller("/notification")
  public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @SubscribeMapping("/notifications")
      public List<NotificationDTO> handleSubscription(@Payload Long userId ,Principal principal) {
      return notificationService.getNotificationsByUsername(principal.getName());
    }

  }