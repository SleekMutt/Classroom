package com.example.classroom.mapper;

import com.example.classroom.dto.course.CourseDTO;
import com.example.classroom.dto.course.CourseToCreateDTO;
import com.example.classroom.dto.course.CourseToUpdateDTO;
import com.example.classroom.entities.Course;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = CoursesUserMapper.class)
public interface CourseMapper {
  Course dtoToEntity(CourseDTO dto);
  Course dtoToEntity(CourseToCreateDTO dto);
  Course dtoToEntity(@MappingTarget Course course, CourseToUpdateDTO dto);
  CourseToUpdateDTO entityToUpdateDto(Course entity);
  CourseDTO entityToDto(Course entity);
}
