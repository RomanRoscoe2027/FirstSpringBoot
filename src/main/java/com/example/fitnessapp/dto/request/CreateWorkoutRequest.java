package com.example.fitnessapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Request body for creating a workout.
 * Only includes fields the client is allowed to send.
 */
@Getter
@Setter
public class CreateWorkoutRequest
{
    @NotBlank(message = "Workout day name is required.")
    private String dayName;

    @NotNull(message = "Workout date is required.")
    private LocalDate date;
}


/*
    Crazy that lombok and validation work together to basically remove the actual boilerplate code needed to validate a request.
    So more or less the endpoint wants a simple request body holding both the dayname and date.
    We validate both ensureing that the dayname isn't an empty string, and that the date is not null.
    From there we can use the request body to create a workout entity and save it to the database.

    The body is used to create a workout entity by J
 */