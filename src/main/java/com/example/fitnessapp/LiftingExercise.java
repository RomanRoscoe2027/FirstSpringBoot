package com.example.fitnessapp;

public class LiftingExercise extends Exercise {

    private int mWeight;

    public LiftingExercise(String name, int reps, int sets, int weight) {
        super(name, reps, sets);
    }

    public int getmWeight() {
        return mWeight;
    }
}
