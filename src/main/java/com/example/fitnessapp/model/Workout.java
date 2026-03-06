package com.example.fitnessapp.model;

import java.util.ArrayList; // the concrete class implementation
import java.util.List; // just an interface providing functionality via, add, size, remove, iterators, etc. Abstraction
import java.time.LocalDate; // allows for grabbing the current date as of right now

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
 *
 * Probably could include setters at some point as well
 */
public class Workout
{
    private int mId = 0;
    // all workouts will have in id, but set default to 0 for now. Will be important for db.
    private String mDayName;
    // all workouts have a name like push, pull, legs etc
    private List<Exercise> mExercises;
    // Have each workout hold a list containing all exercises of object type exercise from exercise class
    // array format for now, but lets us have some flexibility for dev if want to switch
    private LocalDate mDate;
    // typical way to standardize time via java.time package. Will use often

    // Constructor given id and name
    public Workout(int id, String name)
    {
        this.mId = id;
        this.mDayName = name;
        this.mExercises = new ArrayList<Exercise>();
        this.mDate = LocalDate.now();
    }


    ///  GETTERS
    public int getmId()
    {
        return mId;
    }

    public String getmDayName()
    {
        return mDayName;
    }
    public LocalDate getDate()
    {
        return mDate;
    }

    public void LogExercise(Exercise exercise)
    {
        mExercises.add(exercise);
    }

    public List<Exercise> getExercises()
    {
        return mExercises;
    }
    /// END OF GETTERS

    /**
     * Functionality to display workout, but largely depends on individual display funcs
     * via various exercise set types, will be easy to add on with polymorphism if need be.
     *
     * Utilizes StringBuilder ...
     * edit: very cool, Strings are immutable, string builder allows easy and fast rapid growth to a string
     * like object, just need to transform to a string upon returning.
     *
     * @return sb.toString(): Essentially Large paragraph like string containing workout information
     */
    public String displayWorkout() {
        StringBuilder sb = new StringBuilder();
        // create StringBuilder object to continuously add too
        sb.append("Workout Date: ").append(mDate).append("\n");
        sb.append("----------------------------\n");
        // can literally just rapidly append to, NOT an actual string... weird stuff
        for (Exercise exercise : mExercises)
        // sift through all exercises and return only necessary info via getters
        {
            sb.append("Exercise: ").append(exercise.getName()).append("\n");
            int setNumber = 1;
            for (ExerciseSet exerciseSet : exercise.getExerciseSets())
            {
                        sb.append(exerciseSet.display()).append("\n");
                setNumber++;
            }

            sb.append("\n");
        }
        // and because not string, must fully change to string via convienent toString()
        return sb.toString();
    }
}


