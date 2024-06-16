package com.example.classroom.config.security.filter;

import com.example.classroom.config.security.jwt.JwtService;
import com.example.classroom.service.user.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OrganizationPresenceFilter extends OncePerRequestFilter {
  private final JwtService jwtService;
  private final UserServiceImpl userService;
  private final GHOrganization organization;
  private final GitHub gitHub;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    var authHeader = request.getHeader("Authorization");
    if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWithIgnoreCase(authHeader, "Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }
    String jwt = authHeader.substring("Bearer ".length());
    String username = jwtService.extractUserName(jwt);
    if (StringUtils.hasLength(username)) {
      GHUser user = gitHub.getUser(username);
      userService.updateActivationFlagByUsername(username, organization.hasMember(user));
    }
    filterChain.doFilter(request, response);
  }
}

