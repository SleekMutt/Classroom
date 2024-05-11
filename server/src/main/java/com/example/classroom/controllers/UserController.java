package com.example.classroom.controllers;

import com.example.classroom.dto.user.GHUserDTO;
import com.example.classroom.dto.user.UserDTO;
import com.example.classroom.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
  @Autowired
  private UserServiceImpl userServiceImpl;
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/")
  public ResponseEntity<List<UserDTO>> getAllUsers()  {
    return new ResponseEntity<>(userServiceImpl.getAllUsers(), HttpStatus.OK);
  }
  @GetMapping("/{id}")
  public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long id)  {
    return new ResponseEntity<>(userServiceImpl.getUserById(id), HttpStatus.OK);
  }
  @GetMapping("/gh-list")
  public ResponseEntity<Page<GHUserDTO>> getGHUserByLogin(@RequestParam Long courseId, @RequestParam int page) {
    return new ResponseEntity<>(userServiceImpl.getAllGhUserByCourseId(courseId, page), HttpStatus.OK);
  }
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id)  {
    userServiceImpl.deleteUser(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/")
  public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO user)  {
    return new ResponseEntity<>(userServiceImpl.updateUser(user), HttpStatus.OK);
  }
}
