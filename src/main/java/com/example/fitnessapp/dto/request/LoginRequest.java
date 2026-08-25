package com.example.fitnessapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Request body for logging in.
 * The login field can hold either a username or an email.
 */
@Getter
@Setter
public class LoginRequest
{
    @NotBlank(message = "Username or email is required.")
    private String login;

    @NotBlank(message = "Password is required.")
    private String password;
}