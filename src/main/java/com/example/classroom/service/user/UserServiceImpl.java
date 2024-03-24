package com.example.classroom.service.user;

import com.example.classroom.entities.User;
import com.example.classroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements IUserService {
  @Autowired
  private UserRepository userRepository;

  public User createUser(User user) {
    return userRepository.save(user);
  }
  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }
  public User getUserById(Long id) {
    return userRepository.findById(id).orElseThrow(NoSuchElementException::new);
  }
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }
  public User updateUser(User user) {
    return userRepository.save(user);
  }

  public UserDetailsService userDetailsService() {
    return this::loadUser;
  }
  public User loadUser(String username) {
    return getUserByUsername(username);
  }

  public boolean existsUserByUsername(String username) {
      return userRepository.existsUserByGitHubUsername(username);
  }

  @Override
  public User getUserByUsername(String username) {
    return userRepository.getUserByGitHubUsername(username);
  }

  @Override
  public void updateTokenByUsername(String username, String token) {
    userRepository.updateTokenByUsername(username, token);
  }
}
