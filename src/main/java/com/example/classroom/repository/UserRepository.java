package com.example.classroom.repository;

import com.example.classroom.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User getUserByGitHubUsername(String username);
  boolean existsUserByGitHubUsername(String username);
  @Transactional
  @Modifying
  @Query("update User u set u.gitHubToken = :token where u.gitHubUsername = :username")
  void updateTokenByUsername(@Param(value = "username") String username, @Param(value = "token") String token);
}
