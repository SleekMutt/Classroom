package com.example.classroom.mapper;

import com.example.classroom.dto.notification.NotificationDTO;
import com.example.classroom.entities.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = CoursesUserMapper.class)
public interface NotificationMapper {
  NotificationDTO entityToDto(Notification entity);
  Notification dtoToEntity(NotificationDTO dto);
}
