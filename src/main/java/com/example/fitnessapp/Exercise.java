package com.example.fitnessapp;
import java.time.Duration;

public class Exercise
{
    private String mName;
    private int mReps;
    private int mSets;

    public Exercise(String name, int reps, int sets)
    {
        this.mName = name;
        this.mReps = reps;
        this.mSets = sets;
    }

    public String getName() {
        return mName;
    }

    public int getReps() {
        return mReps;
    }

    public int getSets() {
        return mSets;
    }
}
