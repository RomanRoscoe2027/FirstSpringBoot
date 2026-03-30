package com.example.fitnessapp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Request body for registering a new user account.
 * This represents only the fields the client is allowed to send
 * when creating an account.
 */
@Getter
@Setter
public class RegisterRequest
{
    @NotBlank(message = "Username is required.")
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters.")
    private String username;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be valid.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters.")
    private String password;

    @Size(max = 50, message = "First name cannot exceed 50 characters.")
    private String firstName;

    @Size(max = 50, message = "Last name cannot exceed 50 characters.")
    private String lastName;
}
/*
Understanding why these DTOs are needed definitely confuses me when I try to convince myself they are
absolutely necessary.
They are in a way, but mainly for project structure and clean validation. Seperation of these classes from the logic in
the controller or the service is what is really important.
The validation annotations make life so much easier in checking for good inputs.

The other reason is not to expose entities, making it concrete exactly what the user is able to send over
via an endpoint and not a User. Spring is designed to translate requests from these classes via
ex:
@Valid @RequestBody RegisterRequest request
which deserializes through springs http conversion system. Very cool

In other words we keep entities as persistence and db specific, and only expose the data needed for the endpoint.
*/