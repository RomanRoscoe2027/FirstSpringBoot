package com.example.fitnessapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Standard security config for springboot projects.
 * This class encodes passwords via the spring security BCryptPasswordEncoder.
 * Stores ONLY the encoded password in the database.
 *
 * Also uses passWord.matches(encodedPassword) to check if a password matches the encoded password.
 * Can later be adapted via a salt or something, even with match() not entirely sure how that part works
 * yet lol but it's a start.
 */
@Configuration
public class SecurityConfig
{
    @Bean // NOTE INJECTION OF PASSWORD ENCODER.
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
/*
 * Why is this considered a bean?
 *
 * Spring will automatically inject this bean into the UserRepository.
 * This is a good practice to follow.
 * 1. Makes it easier to test.
 * 2. Makes it easier to change the implementation of the password encoder.
 */