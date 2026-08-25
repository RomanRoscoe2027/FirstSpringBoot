package com.example.fitnessapp.exercise_set;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exercises/{exerciseId}/sets")
public class ExerciseSetController {

    private final ExerciseSetService setService;

    public ExerciseSetController(ExerciseSetService setService) {
        this.setService = setService;
    }

    @PostMapping
    @Operation(summary = "Creates a set")
    public ExerciseSet createSet(
            @PathVariable Long exerciseId,
            @RequestBody ExerciseSet exerciseSet) {

        return setService.createSet(exerciseId, exerciseSet);
    }

    @GetMapping
    @Operation(summary = "Get all the sets for a specific exercise.")
    public List<ExerciseSet> getSets(
            @PathVariable Long exerciseId) {
        return setService.getSetsByExercise(exerciseId);
    }
}