package com.example.classroom.mapper;

import com.example.classroom.dto.assignment.AssignmentDTO;
import com.example.classroom.dto.assignment.AssignmentToCreateDTO;
import com.example.classroom.dto.assignment.AssignmentToUpdateDTO;
import com.example.classroom.entities.Assignment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {CourseMapper.class, AssignmentStudentMapper.class})
public interface AssignmentMapper {
  AssignmentDTO entityToDto(Assignment entity);
  Assignment dtoToEntity(AssignmentDTO dto);
  Assignment dtoToEntity(AssignmentToCreateDTO dto);
  Assignment dtoToEntity(@MappingTarget Assignment assignment, AssignmentToUpdateDTO dto);

}
