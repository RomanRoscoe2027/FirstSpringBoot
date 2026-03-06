package com.example.fitnessapp.model;

/**
 * Lifting set derived from class set.
 * May possibly add more ie, reps after failure, time of sets, drop sets, etc.
 * For now this is enough info. Overrides display from base set class
 *
 * Contains, member variables, weight, failure, etc.
 */
public class LiftingExerciseSet extends ExerciseSet {

    /// Weight of barbell, dumbbell, etc
    private double mWeight;

    /// Determines whether a set was done to failure or not.
    private boolean mFailure;


    /**
     * Constructor for a LiftingExerciseSet.
     * @param id
     * @param reps
     * @param weight
     * @param failure
     */
    public LiftingExerciseSet(int id, int reps, double weight, boolean failure) {
        super(id, reps);
        this.mWeight = weight;
        this.mFailure = failure;
    }

    /**
     * Empty Set Constructor
     */
    public LiftingExerciseSet() {
        this.mWeight = 0;
        this.mFailure = false;
    }

    /// GETTERS
    public double getWeight() {
        return mWeight;
    }

    /// SETTERS
    public void setWeight(double mWeight) {
        this.mWeight = mWeight;
    }

    /**
     * Override abstract display func for different string formatting.
     * No need for SB
     * @return string object for display
     */
    @Override
    public String display() {
        return "Lifting Set " + getID() +
                ": " + getReps() +
                " reps at " + mWeight + " lbs" +
                (mFailure ? " | To failure" : " | Not to failure");
    }
}
