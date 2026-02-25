package com.example.fitnessapp;
import com.example.fitnessapp.Exercise;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Workout holds information regarding a specific workout such as:
 * mId = the indicator of each workout for differentiating workouts
 * mDayName = the name of the workout in question, could be split, or simply the day
 * mExcercises = a list that holds the collection of excercises for a workout. Each of object excercise
 *
 * Will hopefully delete default constructor one day when I figure out if there is a
 * point to do so in Java like C++.
 *
 * This workout class is still under development.
 */
public class Workout
{
    private int mId = 0;
    // all workouts will have in id
    private String mDayName;
    // all workouts have a name like push, pull, legs etc
    private List<Exercise> mExercises;
    // Have each workout hold a list containing all exercises of object type exercise from exercise class


    // Constructor given id and name
    public Workout(int id, String name)
    {
        this.mId = id;
        this.mDayName = name;
        this.mExercises = new ArrayList<Exercise>();
    }

    // Add exercise to vector of exercises, those who know do
    public void LogExercise(Exercise exercise)
    {
        mExercises.add(exercise);
    }
    // list of exercises getter
    public List<Exercise> getExercises()
    {
        return mExercises;
    }
}
