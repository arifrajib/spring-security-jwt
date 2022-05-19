package com.arifrajib.springsecurityjwt.service;

import com.arifrajib.springsecurityjwt.dto.LoginRequest;
import com.arifrajib.springsecurityjwt.dto.RegistrationRequest;
import com.arifrajib.springsecurityjwt.entity.User;
import com.arifrajib.springsecurityjwt.repository.UserRepository;
import com.arifrajib.springsecurityjwt.secutiry.JwtUtilityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtUtilityService jwtUtilityService;

    public User userRegistration (RegistrationRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(Instant.now());
        userRepository.save(user);

        return user;
    }

    public String getJwtTokenAfterUserLogin (LoginRequest request) {
        try {
            Authentication authenticationInput = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
            authenticationManager.authenticate(authenticationInput);

            return jwtUtilityService.generateJwtToken(request.getEmail());
        }
        catch (AuthenticationException exception) {
            log.error("User authentication failed, Invalid login credentials..");
            throw new RuntimeException("Invalid login credentials.");
        }
    }
}
