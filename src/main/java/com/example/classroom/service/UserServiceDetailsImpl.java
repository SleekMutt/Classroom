package com.example.classroom.service;

import com.example.classroom.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceDetailsImpl implements UserDetailsService {
  private static final List<User> users = List.of(
          User.builder().username("name").password("password").role("ADMIN").build(),
          User.builder().username("name2").password("password").role("USER").build(),
          User.builder().username("name3").password("password").role("USER").build(),
          User.builder().username("name4").password("password").role("ADMIN").build()
  );
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user1 = users.stream().filter(user ->
            user.getUsername().equals(username)
    ).findFirst().orElseThrow(NoSuchElementException::new);
    return User.builder().username(user1.getUsername()).password(new BCryptPasswordEncoder().encode(user1.getPassword())).role(user1.getRole()).build();
  }
}
