package com.example.fitnessapp.user_workout;

import com.example.fitnessapp.dto.request.CreateWorkoutRequest;
import com.example.fitnessapp.user.User;
import com.example.fitnessapp.user.UserRepository;
import jakarta.transaction.Transactional; // needed for transactional methods, transactional = atomicity

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service; // needed for spring service components.
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional; // designed to protect agains null values, good on http requests

/**
 * Create service of Spring.
 */
@Service
@RequiredArgsConstructor
@Validated // so easy to validate params coming in now for search funcs
public class UserWorkoutService
{
    /// Initialize repository, make final so ref can't be changed
    private final UserWorkoutRepository workoutRepository;
    private final UserRepository userRepository;

    /**
     * Creates a new workout and saves it to the database.
     * Takes in a request body and creates a new workout object from it.
     * @param userId - passed by http request as a path variable
     * @param request - request body containing workout day name and date
     * @return - saved workout object
     */
    @Transactional
    public UserWorkout createWorkout(Long userId, CreateWorkoutRequest request)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found.")); // no user to add workout too?

        // basically all lombok here lol
        UserWorkout workout = new UserWorkout();
        workout.setDayName(request.getDayName().trim());
        workout.setDate(request.getDate());
        workout.setUser(user);

        return workoutRepository.save(workout);
        /*
        saves workout to database with spring CRUD methods, determines between INSERT and UPDATE by checking
        entities @Id field and if it's null, it's an INSERT, otherwise it's an UPDATE.
        */
    }

    /**
     * Finds matching workouts by name via tracker
     * @param name - name of workout to be found
     * @return repo.findWorkoutsMatchingName(name) - list of workouts with matching names
     */
    public List<UserWorkout> findWorkoutsMatchingName(Long userId, String name)
    {
        String trimmedName = requireValidName(name);
        return workoutRepository.findByUserIdAndDayName(userId, trimmedName);
    }

    /**
     * Finds matching workouts by date
     * @param start - start of date time interval
     * @param end - end of date time interval
     * @return repo.findWorkoutsMatchingDate(start, end) - returns new list of all workouts within interval
     */
    public List<UserWorkout> findWorkoutsMatchingDate(Long userId, LocalDate start, LocalDate end)
    {
        requireValidDateRange(start, end);
        return workoutRepository.findByUserIdAndDateRange(userId, start, end);
    }

    /**
     * Removes a workout and returns status of removal.
     *
     * Uses Optional to determine if success of removal of workout, throwing exception
     * if containing null
     * @param name - name of workout to be removed
     * @param date - date of workout to be removed
     * @return matchingWorkout - Optional of workout that was removed, if it exists.
     */
    @Transactional
    public Optional<UserWorkout> removeWorkout(@Positive Long userId, String name, LocalDate date)
    {
        String trimmedName = requireValidName(name);

        if (date == null)
        {
            throw new IllegalArgumentException("Workout date is required.");
        }

        Optional<UserWorkout> matchingWorkout =
                workoutRepository.findByUserIdAndDayNameAndDate(userId, trimmedName, date);

        matchingWorkout.ifPresent(workoutRepository::delete);

        return matchingWorkout;
    }

    /**
     * Validity checking of name, ensure that the name is blank and trimmed.
     * @param name - name of workout
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
     * @param start - start date of interval
     * @param end - end date of interval
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

    public List<UserWorkout> getWorkoutHistory(Long userId)
    {
        return workoutRepository.findByUserId(userId);
    }
}

/*
Can remove these validity checking helper functions and use validation with @NotNull, @NotBlank etc,
HOWEVER:
 business logic like ensuring dates are in correct order, still need these helper funcs.
So gotta be careful on how much I rely on these
nice annotations from spring...
*/