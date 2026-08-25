package com.example.fitnessapp.exercise_set;

import com.example.fitnessapp.workout_exercise.WorkoutExercise;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

/**
 * Class that describes an ExerciseSet.
 */
@Entity
@Data
@Table(name = "exercise_sets")
public class ExerciseSet
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer numberOfReps;

    private Integer numberOfSets;

    private BigDecimal weight;

    private BigDecimal duration;

    private BigDecimal distance;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    @JsonBackReference
    private WorkoutExercise exercise;

    public ExerciseSet(){}
}

