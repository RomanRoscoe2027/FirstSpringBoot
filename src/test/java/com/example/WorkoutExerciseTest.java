package com.example;

import com.example.fitnessapp.Exercise;
import com.example.fitnessapp.ExerciseSet;
import com.example.fitnessapp.LiftingExerciseSet;
import com.example.fitnessapp.CardioExerciseSet;
import com.example.fitnessapp.Workout;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WorkoutExerciseTest
{
    @Test
    void LogExercise() {
        Workout workout = new Workout(0, "Push");
        Exercise benchPress = Exercise.makeLiftingExercise("Bench Press", 3);

        workout.LogExercise(benchPress);

        assertEquals(1, workout.getExercises().size());
        System.out.println(workout.getExercises().size());
        assertEquals("Bench Press", workout.getExercises().get(0).getName());
        System.out.println(workout.getExercises().get(0).getName());
    }

    @Test
    void displayWorkout() {
        Workout workout = new Workout(0, "Push");

        Exercise benchPress = Exercise.makeLiftingExercise("Bench Press", 2);
        Exercise jog = Exercise.makeCardioExercise("Jog", 1);

        ExerciseSet benchSet1 = benchPress.getSet(0);
        if (benchSet1 instanceof LiftingExerciseSet liftingSet) {
            liftingSet.setReps(8);
            liftingSet.setWeight(225.0);
        }

        ExerciseSet benchSet2 = benchPress.getSet(1);
        if (benchSet2 instanceof LiftingExerciseSet liftingSet) {
            liftingSet.setReps(6);
            liftingSet.setWeight(235.0);
        }

        ExerciseSet jogSet1 = jog.getSet(0);
        if (jogSet1 instanceof CardioExerciseSet cardioSet) {
            cardioSet.setReps(1);
            cardioSet.setDuration(Duration.ofMinutes(60));
            cardioSet.setDistance(5.0);
        }

        workout.LogExercise(benchPress);
        workout.LogExercise(jog);

        String expected =
                "Workout Date: " + workout.getDate() + "\n" +
                        "----------------------------\n" +
                        "Exercise: Bench Press\n" +
                        "Lifting Set 1: 8 reps at 225.0 lbs | Not to failure\n" +
                        "Lifting Set 2: 6 reps at 235.0 lbs | Not to failure\n" +
                        "\n" +
                        "Exercise: Jog\n" +
                        "Cardio Set 1: 1 rep(s). Distance 5.0 miles in 60 minutes.\n" +
                        "\n";

        assertEquals(expected, workout.displayWorkout());
    }
}