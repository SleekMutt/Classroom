package com.example.classroom.service.user;

import com.example.classroom.dto.user.UserDTO;
import com.example.classroom.entities.User;
import com.example.classroom.mapper.UserMapper;
import com.example.classroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserMapper userMapper;

  public User createUser(User user) {
    return userRepository.save(user);
  }
  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }
  public UserDTO getUserById(Long id) {
    return userMapper.entityToDto(userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No user was found")));
  }
  public List<UserDTO> getAllUsers() {
    return userRepository.findAll().stream().map(userMapper::entityToDto).collect(Collectors.toList());
  }
  public UserDTO updateUser(UserDTO user) {
    return userMapper.entityToDto(userRepository.save(userMapper.dtoToEntity(user)));
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

  @Override
  public void updateActivationFlagByUsername(String username, boolean flag) {
    userRepository.updateActivationFlagByUsername(username, flag);
  }
}
