package com.example.fitnessapp.workout_exercise;

import com.example.fitnessapp.user_workout.UserWorkout;
import com.example.fitnessapp.user_workout.UserWorkoutRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutExerciseService {

    private final WorkoutExerciseRepository exerciseRepository;
    private final UserWorkoutRepository workoutRepository;

    public WorkoutExerciseService(
            WorkoutExerciseRepository exerciseRepository,
            UserWorkoutRepository workoutRepository) {

        this.exerciseRepository = exerciseRepository;
        this.workoutRepository = workoutRepository;
    }

    public WorkoutExercise createExercise(
            Long workoutId,
            WorkoutExercise exercise) {

        UserWorkout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() ->
                        new RuntimeException("Workout not found"));

        exercise.setWorkout(workout);

        return exerciseRepository.save(exercise);
    }

    public List<WorkoutExercise> getExercisesByWorkout(Long workoutId) {

        return exerciseRepository.findByWorkoutId(workoutId);
    }
}