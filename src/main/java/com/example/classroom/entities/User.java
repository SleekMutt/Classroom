package com.example.classroom.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@Entity
@AllArgsConstructor
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String role;
  @Column(unique = true)
  private String gitHubUsername;
  private String gitHubToken;
  private boolean addedToOrganization;
  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "owner_id")
  private List<Course> ownedCourses;
  @ManyToMany
  @JoinTable(name = "course_student",
          joinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"),
  inverseJoinColumns = @JoinColumn(name = "course_id",
          referencedColumnName = "id"))
  private List<Course> courses;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + role));
  }

  @Override
  public String getPassword() {
    return gitHubToken;
  }

  @Override
  public String getUsername() {
    return gitHubUsername;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return addedToOrganization;
  }
}
