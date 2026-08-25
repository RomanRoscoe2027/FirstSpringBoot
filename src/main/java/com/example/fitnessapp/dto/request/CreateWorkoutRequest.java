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
