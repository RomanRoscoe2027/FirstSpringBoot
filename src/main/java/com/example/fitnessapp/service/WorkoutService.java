package com.example.fitnessapp.service;

import com.example.fitnessapp.model.Workout;
import com.example.fitnessapp.repository.WorkoutRepository;
import org.springframework.stereotype.Service; // needed for spring service components.

import java.time.LocalDate;
import java.util.List;
import java.util.Optional; // designed to protect agains null values, good on http requests

/**
 * Create service of Spring.
 */
@Service
public class WorkoutService
{
    /// Initialize repository, make final so ref can't be changed
    private final WorkoutRepository repository;

    public WorkoutService(WorkoutRepository repository)
    {
        this.repository = repository; /// clear repo dependency
    }

    /**
     * Grabs workout history via tracker from service.
     * @return tracker.getWorkoutHistory - all workout history via list of workouts
     */
    public List<Workout> getWorkoutHistory()
    {
        return repository.findAll();
    }

    /**
     * Adds a workout to the tracker
     * @param name
     * @param date
     * @return workout - the workout which was added
     */
    public Workout addWorkout(String name, LocalDate date)
    {
        String trimmedName = requireValidName(name);
        Workout workout;

        /// Check null date, if so create with just name and current date
        if (date == null)
        {
            workout = new Workout(trimmedName);
        }
        /// Create workout with custom date
        else
        {
            workout = new Workout(trimmedName, date);
        }

        /// add to repository
        repository.save(workout);
        return workout;
    }

    /**
     * Finds matching workouts by name via tracker
     * @param name
     * @return tracker.findWorkoutsMatchingName(name) - list of workouts with matching names
     */
    public List<Workout> findWorkoutsMatchingName(String name)
    {
        String trimmedName = requireValidName(name);
        return repository.findByName(trimmedName);
    }

    /**
     * Finds matching workouts by date
     * @param start
     * @param end
     * @return tracker.findWorkoutsMatchingDate(start, end) - returns new list of all workouts within interval
     */
    public List<Workout> findWorkoutsMatchingDate(LocalDate start, LocalDate end)
    {
        requireValidDateRange(start, end);
        return repository.findByDateRange(start, end);
    }

    /**
     * Removes a workout and returns status of removal.
     *
     * Uses Optional to determine if success of removal of workout, throwing exception
     * if containing null
     * @param name - name of workout to be removed
     * @param date - date of workout to be removed
     * @return boolean - true for workout removed, false otherwise
     */
    public Optional<Workout> removeWorkout(String name, LocalDate date)
    {
        /// Check the name to ensure validity
        String trimmedName = requireValidName(name);
        if (date == null)
        {
            /// can't remove workout without date key
            throw new IllegalArgumentException("Workout date is required.");
        }

        /// Return full history of the repository in new list
        List<Workout> history = repository.findAll();

        for (Workout workout : history)
        {
            boolean sameName = workout.getDayName().equalsIgnoreCase(trimmedName);
            boolean sameDate = workout.getDate().isEqual(date);
            /// check date and name of repository to ensure we have correct one and remove
            if (sameName && sameDate)
            {
                repository.delete(workout);
                return Optional.of(workout); // protects against null
            }
        }

        return Optional.empty(); // delete failed so default to empty
    }

    /**
     * Validity checking of name, ensure that the name is blank and trimmed.
     * @param name
     * @return name.trim() - ensures name without whitespaces
     */
    private String requireValidName(String name)
    {
        if (name == null || name.isBlank())
        {
            throw new IllegalArgumentException("Workout name is required.");
        }
        return name.trim();
    }

    /**
     * Validity checking of date range, if dates aren't valid throw exception
     * @param start
     * @param end
     */
    private void requireValidDateRange(LocalDate start, LocalDate end)
    {
        if (start == null || end == null)
        {
            throw new IllegalArgumentException("Start and end dates are required.");
        }
        if (start.isAfter(end))
        {
            throw new IllegalArgumentException("Start date must be on or before end date.");
        }
    }
}
