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
/*
Basically the process is sending through a string of either the username or the email to login with.
THEN, the server creates a JWT. This JWT is then sent back to the client, which can be used to authenticate future requests.
SO whats sent through is
{
  "login": "roman@email.com",
  "password": "myPlaintextPassword"
} - as a request body which as described in register request spring knows how to deserialize in its http request params

Afterword, the client returns smth like
{
  "token": "eyJhbGciOiJIUzI1NiJ9...", <------- JWT
  "username": "roman123"
} - via auth response about to be implemented as well.

*/