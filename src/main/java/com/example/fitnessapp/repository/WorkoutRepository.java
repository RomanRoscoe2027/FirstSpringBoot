package com.example.fitnessapp.repository;

import com.example.fitnessapp.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
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
    @NativeQuery(""" 
        SELECT * FROM workouts WHERE day_name = :dayName
        """)
    List<Workout> findByDayName(@Param("dayName") String dayName);

    /**
     * Find workouts given an interval of dates.
     * Any workouts falling within the interval will be returned, can be used for one day as well.
     *
     * @param startDate
     * @param endDate
     * @return matchingWorkouts - a list of all workouts within the interval.
     */
    @NativeQuery("""
    SELECT * FROM workouts
    WHERE date >= :startDate
      AND date <= :endDate
    """)
    List<Workout> findByDateRange(@Param("startDate") LocalDate startDate,
                                  @Param("endDate") LocalDate endDate);
}



/*
 * Big difference between native query and JPQL:
 * Native queries are written in SQL and are more flexible but require knowledge of the underlying database schema.
 * JPQL is more abstract and can be used across different databases, but may be less performant for complex queries.
 *
 * Native queries operates on the literal db and schema, wheras JPQL operates on the entity model and relies on JPA
 * to translate it into SQL. JPQL can be more portable across different db systems as a result and typically seem better
 * for more simple queries, but for complex unique queries are less performant, harder to word and more verbose.
 *
 * Overall Native is more performance efficient but obviously takes more knowledge and skill,
 * requiring a deeper understanding of the database schema and SQL syntax.
 * JPQL, on the other hand, is more abstract and can be more portable across different databases,
 * but may sacrifice some performance for the sake of simplicity and readability.\
 *
 * Native = DB specific
 * JPQL = Entity specific
 *
 * AI EXAMPLE ON WHEN TOP USE BOTH:
 * Native SQL example:
 *
 * “I need a window function, CTE, vendor-specific function, or exact DB-tuned query.”
 *
 * JPQL example:
 *
 * “I need users, workouts, exercises, and DTOs in terms of my mapped entities.”
 */