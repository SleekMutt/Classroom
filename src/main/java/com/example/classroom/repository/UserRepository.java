package com.example.classroom.repository;

import com.example.classroom.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User getUserByGitHubUsername(String username);
  boolean existsUserByGitHubUsername(String username);
}
