package com.example.fitnessapp.exercise_set;

import com.example.fitnessapp.workout_exercise.WorkoutExercise;
import com.example.fitnessapp.workout_exercise.WorkoutExerciseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseSetService {

    private final ExerciseSetRepository setRepository;
    private final WorkoutExerciseRepository exerciseRepository;

    public ExerciseSetService(
            ExerciseSetRepository setRepository,
            WorkoutExerciseRepository exerciseRepository) {

        this.setRepository = setRepository;
        this.exerciseRepository = exerciseRepository;
    }

    public ExerciseSet createSet(
            Long exerciseId,
            ExerciseSet exerciseSet) {

        WorkoutExercise exercise =
                exerciseRepository.findById(exerciseId)
                        .orElseThrow(() ->
                                new RuntimeException("Exercise not found"));

        exerciseSet.setExercise(exercise);

        return setRepository.save(exerciseSet);
    }

    public List<ExerciseSet> getSetsByExercise(Long exerciseId) {

        return setRepository.findByExerciseId(exerciseId);
    }
}