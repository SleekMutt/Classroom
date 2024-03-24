package com.example.classroom.controllers;

import com.example.classroom.entities.User;
import com.example.classroom.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
  @Autowired
  private UserServiceImpl userServiceImpl;
  @GetMapping("/")
  public ResponseEntity<List<User>> getAllUsers()  {
    return new ResponseEntity<>(userServiceImpl.getAllUsers(), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable("id") Long id)  {
    return new ResponseEntity<>(userServiceImpl.getUserById(id), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id)  {
    userServiceImpl.deleteUser(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PutMapping("/")
  public ResponseEntity<User> updateUser(@RequestBody User user)  {
    return new ResponseEntity<>(userServiceImpl.updateUser(user), HttpStatus.OK);
  }
}
