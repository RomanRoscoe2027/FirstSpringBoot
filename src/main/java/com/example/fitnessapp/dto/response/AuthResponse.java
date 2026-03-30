package com.example.fitnessapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Response returned after successful authentication.
 * Contains the JWT the client will use on future requests.
 */
@Getter
@AllArgsConstructor
public class AuthResponse
{
    private final String token;
    private final String username;
}
