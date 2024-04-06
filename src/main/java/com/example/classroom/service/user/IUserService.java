package com.example.classroom.service.user;

import com.example.classroom.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUserService {
  User createUser(User user);
  void deleteUser(Long id);
  User getUserById(Long id);
  List<User> getAllUsers();
  User updateUser(User user);
  UserDetailsService userDetailsService();
  User loadUser(String username);
  boolean existsUserByUsername(String username);
  User getUserByUsername(String username);
  void updateTokenByUsername(String username, String token);
  void updateActivationFlagByUsername(String username, boolean flag);
}
