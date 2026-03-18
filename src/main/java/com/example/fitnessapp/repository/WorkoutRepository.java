package com.example.fitnessapp.repository;

import com.example.fitnessapp.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Integer>
{
    /**
     * Find workouts given a name for the day, push, pull, legs, etc.
     * Spring Data JPA automatically implements this based on method name.
     * Trims and doesn't care about caps
     *
     * @param dayName
     * @return matchingWorkouts - a list of all workouts with the same or similar names.
     */
    List<Workout> findByDayName(String dayName);

    /**
     * Find workouts given an interval of dates.
     * Any workouts falling within the interval will be returned, can be used for one day as well.
     *
     * @param startDate
     * @param endDate
     * @return matchingWorkouts - a list of all workouts within the interval.
     */
    @Query("SELECT w FROM Workout w WHERE w.date >= :startDate AND w.date <= :endDate")
    List<Workout> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
