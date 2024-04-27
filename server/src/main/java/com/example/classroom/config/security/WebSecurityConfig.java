package com.example.classroom.config.security;

import com.example.classroom.config.security.filter.JwtAuthenticationFilter;
import com.example.classroom.config.security.filter.OrganizationPresenceFilter;
import com.example.classroom.config.security.handler.AccessDeniedHandlerImpl;
import com.example.classroom.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;
  @Autowired
  private OrganizationPresenceFilter organizationPresenceFilter;
  @Autowired
  private UserServiceImpl userServiceImpl;
  @Autowired
  private AuthEntryPoint authEntryPoint;
  @Autowired
  private AccessDeniedHandlerImpl accessDeniedHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(request -> {
              var corsConfiguration = new CorsConfiguration();
              corsConfiguration.setAllowedOriginPatterns(List.of("*", "http://localhost:3000"));
              corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
              corsConfiguration.setAllowedHeaders(List.of("*"));
              corsConfiguration.setAllowCredentials(true);
              return corsConfiguration;
            }))
            .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
              httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(accessDeniedHandler);
            })
            .authorizeHttpRequests(request -> request
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/test/webhook").permitAll()
                    .anyRequest().authenticated())
            .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(organizationPresenceFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .httpBasic(hbc -> hbc.authenticationEntryPoint(authEntryPoint));
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userServiceImpl.userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
          throws Exception {
    return config.getAuthenticationManager();
  }
}