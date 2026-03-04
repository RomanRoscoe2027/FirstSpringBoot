package com.example.fitnessapp;

/**
 * Lifting set derived from class set.
 * May possibly add more ie, sets after failure, time of sets, drop sets, etc.
 * For now this is enough info. Overrides display from base set class
 *
 * Contains, member variables, weight, failure, etc.
 */
public class LiftingSet extends Set {

    private int mWeight;

    private boolean mFailure;

    public LiftingSet(int id, int reps, int weight, boolean failure) {
        super(id, reps);
        this.mWeight = weight;
        this.mFailure = failure;
    }

    public int getmWeight() {
        return mWeight;
    }

    public void setmWeight(int mWeight) {
        this.mWeight = mWeight;
    }


    /// override abstract display func for different string formatting.
    @Override
    public String display() {
        return "Set " + getID() +
                ": " + getReps() +
                " reps at " + mWeight + " lbs" +
                (mFailure ? " | To failure" : " | Not to failure");
    }
}
