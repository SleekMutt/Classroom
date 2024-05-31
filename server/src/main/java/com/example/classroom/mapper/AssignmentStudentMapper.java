package com.example.classroom.mapper;

import com.example.classroom.dto.assignment.AssignmentStudentDTO;
import com.example.classroom.entities.AssignmentStudent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = CoursesUserMapper.class)
public interface AssignmentStudentMapper {
  AssignmentStudentDTO entityToDto(AssignmentStudent entity);
  AssignmentStudent dtoToEntity(AssignmentStudentDTO dto);

}
