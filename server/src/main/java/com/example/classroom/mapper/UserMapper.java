package com.example.classroom.mapper;

import com.example.classroom.dto.user.UserDTO;
import com.example.classroom.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = CourseMapper.class)
public interface UserMapper {
  User dtoToEntity(UserDTO dto);
  UserDTO entityToDto(User entity);
}
