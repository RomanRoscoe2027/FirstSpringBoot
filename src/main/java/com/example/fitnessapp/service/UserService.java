package com.example.fitnessapp.service;

import com.example.fitnessapp.model.User;
import com.example.fitnessapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Register a new user after validating uniqueness and hashing password.
     * Clean up email and username before saving to db and checking for uniqueness.
     *
     * @param username desired username
     * @param email desired email
     * @param rawPassword plaintext password from request
     * @param firstName optional first name
     * @param lastName optional last name
     * @return saved user
     */
    public User registerUser(String username,
                             String email,
                             String rawPassword,
                             String firstName,
                             String lastName)
    {
        String cleanedUsername = username.trim();
        String cleanedEmail = email.trim().toLowerCase();

        if (userRepository.existsByUsername(cleanedUsername))
        {
            throw new IllegalArgumentException("Username is already taken.");
        }

        if (userRepository.existsByEmail(cleanedEmail))
        {
            throw new IllegalArgumentException("Email is already in use.");
        }

        User user = new User();
        user.setUsername(cleanedUsername);
        user.setEmail(cleanedEmail);
        user.setPasswordHash(passwordEncoder.encode(rawPassword)); // hashes password, ONLY stores encoded hash
        user.setFirstName(firstName);
        user.setLastName(lastName);

        return userRepository.save(user); // saves new user to db
    }

    /**
     * Authenticate a user by username or email plus password.
     * Cleans up and trims login before looking up user.
     *
     * Return a full user entity if login is successful, otherwise throw exception.
     *
     * @param login username or email
     * @param rawPassword plaintext password from request
     * @return authenticated user
     */
    public User loginUser(String login, String rawPassword)
    {
        String cleanedLogin = login.trim();

        User user = userRepository.findByUsernameOrEmail(cleanedLogin)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials."));

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash()))
        {
            throw new IllegalArgumentException("Invalid credentials.");
        }

        return user;
    }

    /**
     * Simple lookup by id, useful when wiring workouts to a user.
     *
     * @param userId user id
     * @return found user
     */
    public User getUserById(Long userId)
    {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
    }
}