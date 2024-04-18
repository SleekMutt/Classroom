package com.example.classroom.service.user;

import com.example.classroom.dto.user.UserDTO;
import com.example.classroom.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUserService {
  User createUser(User user);
  void deleteUser(Long id);
  UserDTO getUserById(Long id);
  List<UserDTO> getAllUsers();
  UserDTO updateUser(UserDTO user);
  UserDetailsService userDetailsService();
  User loadUser(String username);
  boolean existsUserByUsername(String username);
  User getUserByUsername(String username);
  void updateTokenByUsername(String username, String token);
  void updateActivationFlagByUsername(String username, boolean flag);
}
