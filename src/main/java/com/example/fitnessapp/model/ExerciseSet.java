package com.example.fitnessapp.model;

import jakarta.persistence.*;
import lombok.Getter;

/**
 * Discovered that holding sets in exercise did more harm than good. Instead of deriving from exercise,
 * derive from set instead, as each set varies in rep range, weight, etc.
 *
 * A set base can hold reps and holds an id for each set in a series for the given exercise.
 * If there are 3 sets of a bench press, the id can be 1-3.
 * Getters and setters for all mVars.
 */
@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "set_type")
@Table(name = "exercise_sets")
public abstract class ExerciseSet
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /// Making a mvar protected almost always seems bad. Bollocks.
    private int reps;

    /// Directly correlates to size (set number within exercise)
    private int setNumber;

    /// Default constructor allows us to create an exercise, and then fill in the set after
    public ExerciseSet()
    {
        this.reps = 0;
        this.setNumber = 0;
    }

    /// Constructor for set just requires id and rep range
    public ExerciseSet(int id, int reps)
    {
        this.reps = reps;
        this.setNumber = id;
    }

    /*
     The following are the traditional getter and setters.
     */
    public void setID(int setNumber)
    {
        this.setNumber = setNumber;
    }
    public void setReps(int reps) { this.reps = reps;}

    /// Using polymorphism of ExerciseSet, to call display func from each derived version
    public abstract String display();

}

/**
 * KEY POINT:
 *
 */