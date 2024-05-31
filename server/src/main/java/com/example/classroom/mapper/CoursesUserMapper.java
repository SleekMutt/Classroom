package com.example.classroom.mapper;

import com.example.classroom.dto.user.CourseParticipantDTO;
import com.example.classroom.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CoursesUserMapper {
  CourseParticipantDTO entityToCourseParticipant(User entity);
  User courseParticipantToEntity(CourseParticipantDTO dto);
}
