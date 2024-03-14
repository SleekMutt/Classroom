package com.example.classroom.service;

import com.example.classroom.entities.User;
import com.example.classroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  public User createUser(User user){
    return userRepository.save(user);
  }
  public void deleteUser(Long id){
    userRepository.deleteById(id);
  }
  public User updateUser(User user){
    return userRepository.save(user);
  }

  public UserDetailsService userDetailsService(){
    return this::loadUser;
  }
  public User loadUser(String username){
    return userRepository.getUserByGitHubUsername(username);
  }

  public boolean existsUserByUsername(String username) {
      return userRepository.existsUserByGitHubUsername(username);
  }
}
