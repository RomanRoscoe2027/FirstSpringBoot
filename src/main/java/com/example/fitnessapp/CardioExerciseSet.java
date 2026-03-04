package com.example.fitnessapp;

/**
 * Cardio set derived from class set.
 *
 * For now this is enough info. Overrides display from base set class
 *
 * Contains, member variables, duration, distance, etc.
 */
public class CardioSet extends Set {

    private int mDuration;
    private double mDistance;


    public CardioSet(int id, int reps, int duration, double distance) {
        super(id, reps);

        this.mDuration = duration;
        this.mDistance = distance;
    }

    public int getDuration() {
        return mDuration;
    }

    public double getDistance() {
        return mDistance;
    }

    public void setDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    public void setDistance(double mDistance) {
        this.mDistance = mDistance;
    }

    @Override
    public String display() {
        return "Set " + getID() +
                ": " + getReps() +
                " reps." +
                " Distance" + mDistance +
                " in " + mDuration + " minutes.";
    }
}
