package com.arifrajib.springsecurityjwt.controller;

import com.arifrajib.springsecurityjwt.dto.LoginRequest;
import com.arifrajib.springsecurityjwt.dto.RegistrationRequest;
import com.arifrajib.springsecurityjwt.entity.User;
import com.arifrajib.springsecurityjwt.secutiry.JwtUtilityService;
import com.arifrajib.springsecurityjwt.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private AuthService authService;
    private JwtUtilityService jwtUtilityService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> userRegistration (@Valid @RequestBody RegistrationRequest request) {
        User user = authService.userRegistration(request);

        String token = jwtUtilityService.generateJwtToken(user.getEmail());
        Map<String, Object> response = Collections.singletonMap("jwt-token", token);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public Map<String, Object> login (@Valid @RequestBody LoginRequest request) {
        String jwtToken = authService.getJwtTokenAfterUserLogin(request);
        return Collections.singletonMap("token", jwtToken);
    }
}
