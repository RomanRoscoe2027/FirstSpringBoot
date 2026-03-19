package com.example.fitnessapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Lifting set derived from class set.
 * May possibly add more ie, reps after failure, time of sets, drop sets, etc.
 * For now this is enough info. Overrides display from base set class
 *
 * Contains, member variables, weight, failure, etc.
 */
@Getter
@Entity
@DiscriminatorValue("LIFTING")
public class LiftingExerciseSet extends ExerciseSet {

    /// Weight of barbell, dumbbell, etc
    @Setter
    @Column(name = "weight")
    private Double weight;

    /// Determines whether a set was done to failure or not.
    @Setter
    @Column(name = "failure")
    private Boolean failure;


    /**
     * Constructor for a LiftingExerciseSet.
     * @param id
     * @param reps
     * @param weight
     * @param failure
     */
    public LiftingExerciseSet(Integer id, Integer reps, Double weight, Boolean failure) {
        super(id, reps);
        this.weight = weight;
        this.failure = failure;
    }

    /**
     * Empty Set Constructor
     */
    public LiftingExerciseSet()
    {
        super();
    }

    /**
     * Override abstract display func for different string formatting.
     * No need for SB
     * @return string object for display
     */
    @Override
    public String display() {
        return "Lifting Set " + getSetNumber() +
                ": " + getReps() +
                " reps at " + weight + " lbs" +
                (failure ? " | To failure" : " | Not to failure");
    }
}
