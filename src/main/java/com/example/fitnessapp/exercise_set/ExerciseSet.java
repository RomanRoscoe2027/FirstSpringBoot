package com.example.fitnessapp.exercise_set;

import com.example.fitnessapp.workout_exercise.WorkoutExercise;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Class that describes an ExerciseSet. Each ExerciseSet belongs to an exercise, held in its own table.\
 *
 * Design choice: Got rid of the complexity of inherited sets by having just this ExerciseSet class.
 * As a result though, we cram together a lot of fields into this class, and use wrapper data types
 * to allow us to store nulls in db.
 *
 * Unsure how long term this is, but for the sake of me shifting the goal of this project, I think it's a good idea.
 */
@Entity
@Getter
@Table(name = "exercise_sets")
public abstract class ExerciseSet
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private Integer numberOfReps;

    @Setter
    private Integer numberOfSets;

    @Setter
    private BigDecimal weight;

    @Setter
    private BigDecimal duration;

    @Setter
    private BigDecimal distance;

    @Setter
    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false) // foreign key to workout
    @JsonBackReference
    private WorkoutExercise exercise;


    public ExerciseSet(Integer numberOfReps,
                       Integer numberOfSets,
                       BigDecimal weight,
                       BigDecimal duration,
                       BigDecimal distance) {
        this.numberOfReps = numberOfReps;
        this.numberOfSets = numberOfSets;
        this.weight = weight;
        this.duration = duration;
        this.distance = distance;
    }

}

