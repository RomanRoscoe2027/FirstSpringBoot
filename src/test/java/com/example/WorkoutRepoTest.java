package com.example;

import com.example.fitnessapp.model.*;
import com.example.fitnessapp.repository.WorkoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WorkoutRepoTest
{
    private WorkoutRepository repository;

    private Workout pushWorkout;
    private Workout pullWorkout;
    private Workout legsWorkout;

    private Exercise benchPress;
    private Exercise triPushdown;
    private Exercise jog;

    private Exercise deadlift;
    private Exercise latPulldown;
    private Exercise bike;

    private Exercise squat;
    private Exercise legCurl;
    private Exercise stairMaster;

    @BeforeEach
    void setUp()
    {
        repository = new WorkoutRepository();

        pushWorkout = new Workout("Push", LocalDate.of(2026, 3, 1));
        pullWorkout = new Workout("Pull", LocalDate.of(2026, 3, 3));
        legsWorkout = new Workout("Legs", LocalDate.of(2026, 3, 5));

        /// ==================== PUSH WORKOUT ====================
        benchPress = Exercise.makeLiftingExercise("Bench Press", 3);
        setLiftingData(benchPress, 0, 8, 225.0);
        setLiftingData(benchPress, 1, 7, 225.0);
        setLiftingData(benchPress, 2, 4, 255.0);

        triPushdown = Exercise.makeLiftingExercise("Tricep Pushdown", 3);
        setLiftingData(triPushdown, 0, 12, 75.0);
        setLiftingData(triPushdown, 1, 9, 75.0);
        setLiftingData(triPushdown, 2, 8, 75.0);

        jog = Exercise.makeCardioExercise("Jog", 1);
        setCardioData(jog, 0, 1, Duration.ofMinutes(60), 5.0);

        pushWorkout.LogExercise(benchPress);
        pushWorkout.LogExercise(triPushdown);
        pushWorkout.LogExercise(jog);

        /// ==================== PULL WORKOUT ====================
        deadlift = Exercise.makeLiftingExercise("Deadlift", 3);
        setLiftingData(deadlift, 0, 5, 315.0);
        setLiftingData(deadlift, 1, 5, 315.0);
        setLiftingData(deadlift, 2, 3, 365.0);

        latPulldown = Exercise.makeLiftingExercise("Lat Pulldown", 3);
        setLiftingData(latPulldown, 0, 12, 140.0);
        setLiftingData(latPulldown, 1, 10, 140.0);
        setLiftingData(latPulldown, 2, 8, 150.0);

        bike = Exercise.makeCardioExercise("Bike", 1);
        setCardioData(bike, 0, 1, Duration.ofMinutes(25), 8.0);

        pullWorkout.LogExercise(deadlift);
        pullWorkout.LogExercise(latPulldown);
        pullWorkout.LogExercise(bike);

        /// ==================== LEGS WORKOUT ====================
        squat = Exercise.makeLiftingExercise("Squat", 3);
        setLiftingData(squat, 0, 8, 275.0);
        setLiftingData(squat, 1, 6, 295.0);
        setLiftingData(squat, 2, 4, 315.0);

        legCurl = Exercise.makeLiftingExercise("Leg Curl", 3);
        setLiftingData(legCurl, 0, 15, 90.0);
        setLiftingData(legCurl, 1, 12, 100.0);
        setLiftingData(legCurl, 2, 10, 110.0);

        stairMaster = Exercise.makeCardioExercise("StairMaster", 1);
        setCardioData(stairMaster, 0, 1, Duration.ofMinutes(20), 2.0);

        legsWorkout.LogExercise(squat);
        legsWorkout.LogExercise(legCurl);
        legsWorkout.LogExercise(stairMaster);

        repository.save(pushWorkout);
        repository.save(pullWorkout);
        repository.save(legsWorkout);
    }

    private void setLiftingData(Exercise exercise, int setIndex, int reps, double weight)
    {
        ExerciseSet set = exercise.getSet(setIndex);
        assertTrue(set instanceof LiftingExerciseSet, "Expected a LiftingExerciseSet");

        LiftingExerciseSet liftingSet = (LiftingExerciseSet) set;
        liftingSet.setReps(reps);
        liftingSet.setWeight(weight);
    }

    private void setCardioData(Exercise exercise, int setIndex, int reps, Duration duration, double distance)
    {
        ExerciseSet set = exercise.getSet(setIndex);
        assertTrue(set instanceof CardioExerciseSet, "Expected a CardioExerciseSet");

        CardioExerciseSet cardioSet = (CardioExerciseSet) set;
        cardioSet.setReps(reps);
        cardioSet.setDuration(duration);
        cardioSet.setDistance(distance);
    }

    @Test
    void addWorkoutAndGetWorkoutHistoryTest()
    {
        List<Workout> history = repository.findAll();

        assertEquals(3, history.size());
        assertTrue(history.contains(pushWorkout));
        assertTrue(history.contains(pullWorkout));
        assertTrue(history.contains(legsWorkout));
    }

    @Test
    void workoutGetterTest()
    {
        assertEquals("Push", pushWorkout.getDayName());
        assertEquals(LocalDate.of(2026, 3, 1), pushWorkout.getDate());
        assertEquals(3, pushWorkout.getExercises().size());

        assertEquals("Pull", pullWorkout.getDayName());
        assertEquals(LocalDate.of(2026, 3, 3), pullWorkout.getDate());
        assertEquals(3, pullWorkout.getExercises().size());

        assertEquals("Legs", legsWorkout.getDayName());
        assertEquals(LocalDate.of(2026, 3, 5), legsWorkout.getDate());
        assertEquals(3, legsWorkout.getExercises().size());
    }

    @Test
    void pushWorkoutLiftingSetDataTest()
    {
        assertEquals("Bench Press", benchPress.getName());
        assertEquals("Tricep Pushdown", triPushdown.getName());

        LiftingExerciseSet benchSet1 = (LiftingExerciseSet) benchPress.getSet(0);
        LiftingExerciseSet benchSet2 = (LiftingExerciseSet) benchPress.getSet(1);
        LiftingExerciseSet benchSet3 = (LiftingExerciseSet) benchPress.getSet(2);

        assertEquals(8, benchSet1.getReps());
        assertEquals(225.0, benchSet1.getWeight(), 0.001);

        assertEquals(7, benchSet2.getReps());
        assertEquals(225.0, benchSet2.getWeight(), 0.001);

        assertEquals(4, benchSet3.getReps());
        assertEquals(255.0, benchSet3.getWeight(), 0.001);

        LiftingExerciseSet pushdownSet1 = (LiftingExerciseSet) triPushdown.getSet(0);
        LiftingExerciseSet pushdownSet2 = (LiftingExerciseSet) triPushdown.getSet(1);
        LiftingExerciseSet pushdownSet3 = (LiftingExerciseSet) triPushdown.getSet(2);

        assertEquals(12, pushdownSet1.getReps());
        assertEquals(75.0, pushdownSet1.getWeight(), 0.001);

        assertEquals(9, pushdownSet2.getReps());
        assertEquals(75.0, pushdownSet2.getWeight(), 0.001);

        assertEquals(8, pushdownSet3.getReps());
        assertEquals(75.0, pushdownSet3.getWeight(), 0.001);
    }

    @Test
    void cardioSetDataTest()
    {
        CardioExerciseSet jogSet = (CardioExerciseSet) jog.getSet(0);
        assertEquals(1, jogSet.getReps());
        assertEquals(Duration.ofMinutes(60), jogSet.getDuration());
        assertEquals(5.0, jogSet.getDistance(), 0.001);

        CardioExerciseSet bikeSet = (CardioExerciseSet) bike.getSet(0);
        assertEquals(1, bikeSet.getReps());
        assertEquals(Duration.ofMinutes(25), bikeSet.getDuration());
        assertEquals(8.0, bikeSet.getDistance(), 0.001);

        CardioExerciseSet stairSet = (CardioExerciseSet) stairMaster.getSet(0);
        assertEquals(1, stairSet.getReps());
        assertEquals(Duration.ofMinutes(20), stairSet.getDuration());
        assertEquals(2.0, stairSet.getDistance(), 0.001);
    }

    @Test
    void eachWorkoutContainsBothLiftingAndCardioTest()
    {
        for (Workout workout : repository.findAll())
        {
            boolean hasLifting = false;
            boolean hasCardio = false;

            for (Exercise exercise : workout.getExercises())
            {
                ExerciseSet firstSet = exercise.getSet(0);

                if (firstSet instanceof LiftingExerciseSet)
                {
                    hasLifting = true;
                }

                if (firstSet instanceof CardioExerciseSet)
                {
                    hasCardio = true;
                }
            }

            assertTrue(hasLifting, workout.getDayName() + " should contain a lifting exercise");
            assertTrue(hasCardio, workout.getDayName() + " should contain a cardio exercise");
        }
    }

    @Test
    void findWorkoutsByDateRangeTest()
    {
        // Assumes tracker method signature is:
        // List<Workout> findWorkoutsByDate(LocalDate startDate, LocalDate endDate)

        List<Workout> results = repository.findByDateRange(
                LocalDate.of(2026, 3, 2),
                LocalDate.of(2026, 3, 5)
        );

        assertEquals(2, results.size());
        assertTrue(results.contains(pullWorkout));
        assertTrue(results.contains(legsWorkout));
        assertFalse(results.contains(pushWorkout));
    }

    @Test
    void findWorkoutsByMatchingNameTest()
    {
        // Assumes tracker method signature is:
        // List<Workout> findWorkoutsByName(String name)

        List<Workout> results = repository.findByName("Push");

        assertEquals(1, results.size());
        assertEquals("Push", results.get(0).getDayName());
        assertEquals(LocalDate.of(2026, 3, 1), results.get(0).getDate());
    }

    @Test
    void findWorkoutsByNameNoMatchTest()
    {
        List<Workout> results = repository.findByName("Chest");

        assertTrue(results.isEmpty());
    }

    @Test
    void displayWorkoutContainsExpectedTextTest()
    {
        String display = pushWorkout.displayWorkout();

        assertTrue(display.contains("Workout Date: 2026-03-01"));
        assertTrue(display.contains("Exercise: Bench Press"));
        assertTrue(display.contains("Exercise: Tricep Pushdown"));
        assertTrue(display.contains("Exercise: Jog"));
    }

    @Test
    void printAllWorkoutsTest()
    {
        for (Workout workout : repository.findAll())
        {
            System.out.println("=================================");
            System.out.println("Workout Name: " + workout.getDayName());
            System.out.println(workout.displayWorkout());
        }
    }
}
