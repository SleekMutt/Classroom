package com.example.classroom.service.user;

import com.example.classroom.dto.user.GHUserDTO;
import com.example.classroom.dto.user.UserDTO;
import com.example.classroom.entities.User;
import com.example.classroom.mapper.UserMapper;
import com.example.classroom.repository.UserRepository;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserMapper userMapper;
  @Autowired
  private GitHub gitHub;

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
  public Page<GHUserDTO> getAllGhUserByCourseId(Long courseId, int pageNumber) {
    Page<User> allByCourseId = userRepository.findAllByCourseId(courseId, PageRequest.of(pageNumber, 5));
    return allByCourseId.map(user -> {
      try {
        GHUserDTO ghUserDTO = userMapper.entityToDto(gitHub.getUser(user.getGitHubUsername()));
        ghUserDTO.setId(user.getId());
        return ghUserDTO;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }
}
