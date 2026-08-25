package com.example.fitnessapp.exercise_set;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseSetRepository
        extends JpaRepository<ExerciseSet, Long> {

    List<ExerciseSet> findByExerciseId(Long exerciseId);
}