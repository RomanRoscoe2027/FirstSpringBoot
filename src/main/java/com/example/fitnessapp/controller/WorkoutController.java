package com.example.fitnessapp.controller;

import com.example.fitnessapp.dto.request.CreateWorkoutRequest;
import com.example.fitnessapp.model.Workout;
import com.example.fitnessapp.service.WorkoutService;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat; // for iso datetime from localtime
import org.springframework.http.ResponseEntity; // sending back responses from http as json
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/workouts") // NEEDS USER ID FOR PATH VARIABLE
public class WorkoutController
{
    /// create controllers workout service dependency, final reference
    private final WorkoutService workoutService;

    /// construct controllers final workout service reference
    public WorkoutController(WorkoutService workoutService)
    {
        this.workoutService = workoutService; /// clear service dependency
    }

    /**
     * Handles GET /api/workouts.
     * @return all workouts as JSON.
     */
    @GetMapping
    public List<Workout> getUserWorkouts(@PathVariable Long userId)
    {
        // rip bean comment
        return workoutService.getWorkoutHistory(userId);
    }

    /**
     * Handles POST /api/workouts.
     * Integrates with adding exercises and sets by receiving a full Workout object in the body.
     * @param userId - user id of the workout, notice its made as a path variable, aka no optional and needed for every request.
     * @param request - we create a new workout object from this request body using the CreateWorkoutRequest DTO.
     * @return - saved workout day and date
     */
    @PostMapping
    public Workout createWorkout(@PathVariable Long userId, @Valid @RequestBody CreateWorkoutRequest request)
    {
        return workoutService.createWorkout(userId, request);
    }

    /**
     * Handles GET /api/workouts/search/matchingname?name=Push
     * @param name - Whatever the name of workout is
     * @return List<Workout>- All workouts with the same name
     */
    @GetMapping("/search/name")
    public List<Workout> searchByMatchingName(@PathVariable Long userId, @RequestParam String name)
    {
        return workoutService.findWorkoutsMatchingName(userId, name);
    }

    /**
     * Handles GET /api/workouts/search/date?start=2026-03-01&end=2026-03-05
     * @param start - start of date time interval
     * @param end - end of date time interval
     * @return List<Workout>- All workouts within the interval
     */
    @GetMapping("/search/date")
    public List<Workout> searchByDate(
            @PathVariable Long userId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end)
    {
        return workoutService.findWorkoutsMatchingDate(userId, start, end);
    }

    /**
     * Handles DELETE /api/workouts?name=Push&date=2026-03-01.
     * Deletes a workout given a date.
     * @param name - name of workout to be removed
     * @param date - date of workout to be removed
     * @return - either http200 with the removed workout, or a failure http code
     */
    @DeleteMapping
    public ResponseEntity<Workout> deleteWorkout(
            @PathVariable Long userId,
            @RequestParam String name,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
    {
        /// removed workout needed for response entity
        var removed = workoutService.removeWorkout(userId, name, date);
        /// var type like auto, good for Optional returns that could vary
        /// ok message with removed workout
        return removed.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

        ///  uses build preset for 404 error, aka no body
        ///  if want body can change to .body() instead
    }
}
