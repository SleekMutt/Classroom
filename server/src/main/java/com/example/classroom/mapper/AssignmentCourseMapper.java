package com.example.classroom.mapper;

import com.example.classroom.dto.course.CourseToUpdateDTO;
import com.example.classroom.entities.Course;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AssignmentCourseMapper {
  Course dtoToEntity(@MappingTarget Course course, CourseToUpdateDTO dto);
  CourseToUpdateDTO entityToUpdateDto(Course entity);
}
