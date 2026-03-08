package com.example.fitnessapp.controller;

import com.example.fitnessapp.model.Workout;
import com.example.fitnessapp.service.WorkoutService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController
{
    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService)
    {
        this.workoutService = workoutService;
    }

    @GetMapping
    public List<Workout> getAllWorkouts()
    {
        return workoutService.getWorkoutHistory();
    }

    @PostMapping
    public Workout createWorkout(
            @RequestParam String name,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
    {
        return workoutService.addWorkout(name, date);
    }

    @GetMapping("/search/name")
    public List<Workout> searchByName(@RequestParam String value)
    {
        return workoutService.findWorkoutsMatchingName(value);
    }

    @GetMapping("/search/date")
    public List<Workout> searchByDate(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end)
    {
        return workoutService.findWorkoutsMatchingDate(start, end);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteWorkout(
            @RequestParam String name,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
    {
        boolean removed = workoutService.removeWorkout(name, date);

        if (removed)
        {
            return ResponseEntity.ok("Workout removed.");
        }

        return ResponseEntity.notFound().build();
    }
}