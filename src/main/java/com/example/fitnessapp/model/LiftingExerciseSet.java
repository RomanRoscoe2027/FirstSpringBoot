package com.example.fitnessapp.model;

import jakarta.persistence.*;

/**
 * Lifting set derived from class set.
 * May possibly add more ie, reps after failure, time of sets, drop sets, etc.
 * For now this is enough info. Overrides display from base set class
 *
 * Contains, member variables, weight, failure, etc.
 */
@Entity
@DiscriminatorValue("LIFTING")
public class LiftingExerciseSet extends ExerciseSet {

    /// Weight of barbell, dumbbell, etc
    private double weight;

    /// Determines whether a set was done to failure or not.
    private boolean failure;


    /**
     * Constructor for a LiftingExerciseSet.
     * @param id
     * @param reps
     * @param weight
     * @param failure
     */
    public LiftingExerciseSet(int id, int reps, double weight, boolean failure) {
        super(id, reps);
        this.weight = weight;
        this.failure = failure;
    }

    /**
     * Empty Set Constructor
     */
    public LiftingExerciseSet() {
        this.weight = 0;
        this.failure = false;
    }

    /// GETTERS
    public double getWeight() {
        return weight;
    }

    /// SETTERS
    public void setWeight(double mWeight) {
        this.weight = mWeight;
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
