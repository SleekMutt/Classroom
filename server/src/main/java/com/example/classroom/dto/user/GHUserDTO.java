package com.example.classroom.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GHUserDTO {
    private Long id;
    private String login;
    private String avatarUrl;
}