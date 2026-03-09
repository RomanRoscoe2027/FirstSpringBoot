package com.example.fitnessapp.controller;

import com.example.fitnessapp.model.Workout;
import com.example.fitnessapp.service.WorkoutService;

import org.springframework.format.annotation.DateTimeFormat; // for iso datetime from localtime
import org.springframework.http.ResponseEntity; // sending back responses from http as json
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/workouts") // sets base url path for every controller endpoint
/**
 * Workout controller class. RestController combines both Controller and ResponseBody endpoints
 * Manages both HTTP requests but also writes back to HTTP response body, effectively
 * combining responsibilities.
 * Currently, has:
 * GET,POST,DELETE mapping
 */
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
    public List<Workout> getAllWorkouts()
    {
        /// need workout service bean lol. why bean be the name we refer too
        return workoutService.getWorkoutHistory();
    }

    /**
     * Handles POST /api/workouts?name=Push&date=2026-03-01
     * @param name - name of workout, push, pull, legs
     * @param date - local date time of workout, either now as default blank, or custom date
     * @return - calls the service to create a workout and returns said workout
     */
    @PostMapping
    public Workout createWorkout(
            @RequestParam String name,
            @RequestParam(required = false)
            /// DateTimeFormat
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
    {
        return workoutService.addWorkout(name, date);
    }

    /**
     * Handles GET /api/workouts/search/matchingname?name=Push
     * @param name - Whatever the name of workout is
     * @return List<Workout>- All workouts with the same name
     */
    @GetMapping("/search/matchingname")
    public List<Workout> searchByMatchingName(@RequestParam String name)
    {
        return workoutService.findWorkoutsMatchingName(name);
    }

    /**
     * Handles GET /api/workouts/search/date?start=2026-03-01&end=2026-03-05
     * @param start - start of date time interval
     * @param end - end of date time interval
     * @return List<Workout>- All workouts within the interval
     */
    @GetMapping("/search/date")
    public List<Workout> searchByDate(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end)
    {
        return workoutService.findWorkoutsMatchingDate(start, end);
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
            @RequestParam String name,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
    {
        /// removed workout needed for response entity
        var removed = workoutService.removeWorkout(name, date);
        /// var type like auto, good for Optional returns that could vary
        if (removed.isPresent())
        {
            /// ok message with removed workout
            return ResponseEntity.ok(removed.get());
        }

        return ResponseEntity.notFound().build();
        ///  uses build preset for 404 error, aka no body
        ///  if want body can change to .body() instead
    }
}
