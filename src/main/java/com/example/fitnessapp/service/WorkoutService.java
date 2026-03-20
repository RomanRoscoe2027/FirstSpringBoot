package com.example.fitnessapp.service;

import com.example.fitnessapp.model.User;
import com.example.fitnessapp.model.Workout;
import com.example.fitnessapp.repository.WorkoutRepository;
import com.example.fitnessapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service; // needed for spring service components.

import java.time.LocalDate;
import java.util.List;
import java.util.Optional; // designed to protect agains null values, good on http requests

/**
 * Create service of Spring.
 */
@Service
@RequiredArgsConstructor
public class WorkoutService
{
    /// Initialize repository, make final so ref can't be changed
    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;

    @Transactional
    public Workout addWorkoutToUser(Long userId, String dayName)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        Workout workout = new Workout();
        workout.setDayName(dayName);
        workout.setUser(user);

        return workoutRepository.save(workout);
    }


    /**
     * Finds matching workouts by name via tracker
     * @param name
     * @return tracker.findWorkoutsMatchingName(name) - list of workouts with matching names
     */
    public List<Workout> findWorkoutsMatchingName(String name)
    {
        String trimmedName = requireValidName(name);
        return workoutRepository.findByDayName(trimmedName);
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
        return workoutRepository.findByDateRange(start, end);
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
    @Transactional
    public Optional<Workout> removeWorkout(Long userId, String name, LocalDate date)
    {
        String trimmedName = requireValidName(name);

        if (date == null)
        {
            throw new IllegalArgumentException("Workout date is required.");
        }

        Optional<Workout> matchingWorkout =
                workoutRepository.findByUserIdAndDayNameAndDate(userId, trimmedName, date); // NOT IMPLEMENTED WILL COME BACK TO. JUST HAVE TO ADD USER ID WITH QUERY

        matchingWorkout.ifPresent(workoutRepository::delete);

        return matchingWorkout;
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
