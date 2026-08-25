package com.example.fitnessapp.workout_exercise;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workouts/{workoutId}/exercises")
public class WorkoutExerciseController {

    private final WorkoutExerciseService exerciseService;

    public WorkoutExerciseController(
            WorkoutExerciseService exerciseService) {

        this.exerciseService = exerciseService;
    }

    @PostMapping
    @Operation(summary = "Creates an exercise for a sepcific workout according to a workout id.")
    public WorkoutExercise createExercise(
            @PathVariable Long workoutId,
            @RequestBody WorkoutExercise exercise) {

        return exerciseService.createExercise(
                workoutId,
                exercise
        );
    }

    @GetMapping
    @Operation(summary = "Get all the exercises for a specific workout.")
    public List<WorkoutExercise> getExercises(
            @PathVariable Long workoutId) {

        return exerciseService.getExercisesByWorkout(workoutId);
    }
}