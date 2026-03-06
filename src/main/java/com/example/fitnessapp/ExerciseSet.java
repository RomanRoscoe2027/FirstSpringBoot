package com.example.fitnessapp;

/**
 * Discovered that holding sets in exercise did more harm than good. Instead of deriving from exercise,
 * derive from set instead, as each set varies in rep range, weight, etc.
 *
 * A set base can hold reps and holds an id for each set in a series for the given exercise.
 * If there are 3 sets of a bench press, the id can be 1-3.
 * Getters and setters for all mVars.
 */
public abstract class ExerciseSet
{
    /// Making a mvar protected almost always seems bad. Bollocks.
    private int mReps;

    /// Directly correlates to size
    private int mID;

    /// Default constructor allows us to create an exercise, and then fill in the set after
    public ExerciseSet() {
        this.mReps = 0;
        this.mID = 0;
    }

    /// Constructor for set just requires id and rep range
    public ExerciseSet(int id, int reps)
    {
        this.mReps = reps;
        this.mID = id;
    }

    /*
     The following are the traditional getter and setters.
     */
    public int getID() {
        return mID;
    }
    public void setID(int mID) {
        this.mID = mID;
    }
    public int getReps() {
        return mReps;
    }
    public void setReps(int mReps) {
        this.mReps = mReps;
    }

    /// Using polymorphism of ExerciseSet, to call display func from each derived version
    public abstract String display();

}
