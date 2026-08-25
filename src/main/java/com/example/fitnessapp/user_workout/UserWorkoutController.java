package com.example.fitnessapp.user_workout;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/workouts")
public class UserWorkoutController {

    private final UserWorkoutService workoutService;

    public UserWorkoutController(UserWorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @PostMapping
    @Operation(summary = "Create a workout for a user.")
    public UserWorkout createWorkout(
            @PathVariable Long userId,
            @RequestBody UserWorkout workout) {

        return workoutService.createWorkout(userId, workout);
    }

    @GetMapping
    @Operation(summary = "Gets all the workouts for a user.")
    public List<UserWorkout> getWorkouts(
            @PathVariable Long userId) {

        return workoutService.getWorkoutsByUser(userId);
    }
}