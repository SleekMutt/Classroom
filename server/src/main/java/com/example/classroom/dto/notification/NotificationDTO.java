package com.example.classroom.dto.notification;


import com.example.classroom.dto.user.CourseParticipantDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class NotificationDTO {
  private Long id;
  private String message;
  private LocalDateTime sentDate;
  private CourseParticipantDTO user;
}
