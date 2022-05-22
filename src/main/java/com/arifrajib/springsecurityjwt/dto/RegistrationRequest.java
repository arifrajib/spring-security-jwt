package com.arifrajib.springsecurityjwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {
    @NotBlank(message = "User name is required")
    private String name;
    @Email
    @NotEmpty(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
}
