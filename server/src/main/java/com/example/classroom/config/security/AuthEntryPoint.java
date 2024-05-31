package com.example.classroom.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Component
@RequiredArgsConstructor
public class AuthEntryPoint implements AuthenticationEntryPoint {
  final ObjectMapper objectMapper;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    Map<String, ?> message = new HashMap<>() {{
      put("status", HttpServletResponse.SC_UNAUTHORIZED);
      put("error", "Unauthorized");
      put("message", authException.getMessage());
      put("path", request.getServletPath());
    }};

    objectMapper.writeValue(response.getOutputStream(), message);
  }
}
