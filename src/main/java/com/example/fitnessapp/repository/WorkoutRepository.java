package com.example.fitnessapp.repository;

import com.example.fitnessapp.model.Workout;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList; // the concrete class implementation
import java.util.List; // just an interface providing functionality via, add, size, remove, iterators, etc. Abstraction

@Repository
public class WorkoutRepository
{
    /// List containing all workouts
    private List<Workout> mWorkouts;

    /// Getter for mWorkouts
    public List<Workout> findAll()
    {
        return new ArrayList<>(mWorkouts);
    }

    /**
     * Constructor for workout repository to initialize.
     * Will probably make it its own initialize function of sorts on app startup.
     */
    public WorkoutRepository()
    {
        mWorkouts = new ArrayList<>();
    }

    /**
     * Function adds a workout to our repository.
     * Error checks for no empty workout.
     * @param workout
     */
    public void save(Workout workout)
    {
        if (workout == null)
        {
            throw new IllegalArgumentException("Workout cannot be null");
        }
        mWorkouts.add(workout);
    }

    /**
     * Remove a workout from the repository.
     * @param workout
     */
    public void delete(Workout workout)
    {
        mWorkouts.remove(workout);
    }

    /**
     * Find workouts given a name for the day, push, pull, legs, etc.
     * Match the name to all workouts with said name and return a list of workouts with the same name.
     * Trims and doesn't care about caps
     * Don't modify actual repository, return a new list ofc.
     *
     * @param workoutName
     * @return matchingWorkouts - a list of all workouts with the same or similar names.
     */
    public List<Workout> findByName(String workoutName)
    {
        /// Create return list
        List<Workout> matchingWorkouts = new ArrayList<>();

        /// Ensure workout name is not empty for search
        if (workoutName == null || workoutName.isBlank())
        {
            return matchingWorkouts;
        }

        /// Sift through all workouts, don't care about capitalization or white space as long as chars order correct
        /// ex: Legs, vs lEG s.
        for (Workout workout : mWorkouts)
        {
            if (workout.getDayName().equalsIgnoreCase(workoutName.trim()))
            {
                matchingWorkouts.add(workout);
            }
        }
        return matchingWorkouts;
    }

    /**
     * Find workouts given an interval of dates.
     * Any workouts falling within the interval will be returned, can be used for one day as well.
     * (BUT LIKELY WILL SEPERATE ONE DAY FROM MANY DAYS, FRONTEND)
     * Don't modify actual repository, return a new list ofc.
     *
     * @param startDate
     * @param endDate
     * @return matchingWorkouts - a list of all workouts within the interval.
     */
    public List<Workout> findByDateRange(LocalDate startDate, LocalDate endDate)
    {
        /// Create return list
        List<Workout> matchingWorkouts = new ArrayList<>();

        /// Valid date interval not empty
        if (startDate == null || endDate == null)
        {
            return matchingWorkouts;
        }

        /// Incorrect ordering of dates or misinput of nums
        if (startDate.isAfter(endDate))
        {
            System.out.println("Dates Reversed or Incorrect.");
        }

        /// Sift through all workouts and extract dates
        for (Workout workout : mWorkouts)
        {
            LocalDate workoutDate = workout.getDate();

            /// interval checking
            boolean onOrAfterStart = workoutDate.isEqual(startDate) || workoutDate.isAfter(startDate);
            boolean onOrBeforeEnd = workoutDate.isEqual(endDate) || workoutDate.isBefore(endDate);

            /// Is date within the interval
            if (onOrAfterStart && onOrBeforeEnd)
            {
                matchingWorkouts.add(workout);
            }
        }
        return matchingWorkouts;
    }
}
