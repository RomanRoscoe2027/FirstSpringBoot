package com.example.fitnessapp;

public class CardioExercise extends Exercise {

    private int mDuration;
    private double mDistance;


    public CardioExercise(String name, int reps, int sets, int duration, double distance) {
        super(name, reps, sets);

        this.mDuration = duration;
        this.mDistance = distance;
    }

    public int getmDuration() {
        return mDuration;
    }


    public double getmDistance() {
        return mDistance;
    }

}
