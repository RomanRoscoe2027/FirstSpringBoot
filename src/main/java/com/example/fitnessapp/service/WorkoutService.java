package com.example.fitnessapp.service;

import com.example.fitnessapp.model.Workout;
import com.example.fitnessapp.repository.WorkoutRepository;
import org.springframework.stereotype.Service; // needed for spring service components.

import java.time.LocalDate;
import java.util.List;

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
        this.repository = repository;
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
     * Adds a workout to the tracker/
     * @param name
     * @param date
     * @return workout - the workout which was added
     */
    public Workout addWorkout(String name, LocalDate date)
    {
        Workout workout;

        /// Check null date, if so create with just name and current date
        if (date == null)
        {
            workout = new Workout(name);
        }
        /// Create workout with custome date
        else
        {
            workout = new Workout(name, date);
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
        return repository.findByName(name);
    }

    /**
     * Finds matching workouts by date
     * @param start
     * @param end
     * @return tracker.findWorkoutsMatchingDate(start, end) - returns new list of all workouts within interval
     */
    public List<Workout> findWorkoutsMatchingDate(LocalDate start, LocalDate end)
    {
        return repository.findByDateRange(start, end);
    }

    /**
     * Removes a workout and returns status of removal
     * @param name
     * @param date
     * @return boolean - true for workout removed, false otherwise
     */
    public boolean removeWorkout(String name, LocalDate date)
    {
        List<Workout> history = repository.findAll();

        for (Workout workout : history)
        {
            boolean sameName = workout.getDayName().equalsIgnoreCase(name.trim());
            boolean sameDate = workout.getDate().isEqual(date);

            if (sameName && sameDate)
            {
                repository.delete(workout);
                return true;
            }
        }

        return false;
    }
}
