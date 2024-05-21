package com.example.classroom.dto.comment;

import com.example.classroom.dto.user.GHUserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommentDTO {
  private String body;
  private Date createdAt;
  private GHUserDTO user;
}
