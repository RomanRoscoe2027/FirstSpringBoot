package com.example;

import com.example.fitnessapp.Exercise;
import com.example.fitnessapp.Workout;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WorkoutExerciseTest
{
    @Test
    void LogExercise() {
        Workout workout = new Workout(0, "Push");
        Exercise benchPress = new Exercise("Bench Press", 8, 3);

        workout.LogExercise(benchPress);

        assertEquals(1, workout.getExercises().size());
        System.out.println(workout.getExercises().size());
        assertEquals("Bench Press", workout.getExercises().get(0).getName());
        System.out.println(workout.getExercises().get(0).getName());
    }
}