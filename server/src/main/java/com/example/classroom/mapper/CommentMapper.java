package com.example.classroom.mapper;

import com.example.classroom.dto.comment.CommentDTO;
import org.kohsuke.github.GHIssueComment;
import org.mapstruct.Mapper;

import java.io.IOException;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface CommentMapper {
  GHIssueComment dtoToEntity(CommentDTO dto) throws IOException;
  CommentDTO entityToDto(GHIssueComment entity) throws IOException;
}
