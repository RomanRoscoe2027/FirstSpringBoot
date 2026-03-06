package com.example.fitnessapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Describes an Exercise. Holds all sets via an array.
 */
public class Exercise
{
    /// Name of the exercise
    private String mName;
    /// Dynamic array containing all sets for an exercise
    private List<ExerciseSet> mExerciseSets;

    /**
     * FULL LEARNING AND DESCRIPTION:
     * The type of exercise grabbed via a factory.
     * Functional Interface that comes from java.util.function.
     * Supplier<T> - represents something that supplies an object.
     *
     * Has one key method - T get();
     *
     * Supplier<ExerciseSet> factory = () -> new ExerciseSet();
     * ExerciseSet set = factory.get();
     *
     * Basically factory becomes recipe/wrapper for an exercise set.
     * So, Supplier </? extends ExerciseSet/>  gives functionality to allow flexibility of construction.
     * Providing construction for either a cardio or a lifting set, as both extend ExerciseSet
     * Then calls addEmptySet which allocates for our list.
     *
     * If have more set variants, allows for it.
     */
    private Supplier<? extends ExerciseSet> mExerciseSetFactory;
    public Exercise(String name, int numberOfSets, Supplier<? extends ExerciseSet> factory)
    {
        /// Name of exercise
        this.mName = name;

        /// Set array
        this.mExerciseSets = new ArrayList<>();

        this.mExerciseSetFactory = factory;
        /// Given user set number, create a set where we allocate amount of set objects to default 0's
        /// Later user will fill in given information for sets
        for (int i = 0; i < numberOfSets; i++)
        {
           /*
            * mExerciseSets.add(new ExerciseSet());  <--- ORIGINAL IDEA. Cannot work as a result of ExerciseSet becoming abstract.
            * need to use a protected method that overrides set abstract creation of list for typing at runtime
            * Instead, we do below for polymorphism at runtime
            */
            addEmptySet();
        }
    }

    /**
     * Creates an Exercise that uses LiftingExerciseSet objects.
     * @param name
     * @param numberOfSets
     * @return LiftingExerciseSet
     */
    public static Exercise makeLiftingExercise(String name, int numberOfSets)
    {
        return new Exercise(name, numberOfSets, LiftingExerciseSet::new);
    }

    /**
     * Creates an Exercise that uses CardioExerciseSet objects.
     * @param name
     * @param numberOfSets
     * @return
     */
    public static Exercise makeCardioExercise(String name, int numberOfSets)
    {
        return new Exercise(name, numberOfSets, CardioExerciseSet::new);
    }

    /// GETTER START
    public String getName()
    {
        return mName;
    }

    public List<ExerciseSet> getExerciseSets()
    {
        return mExerciseSets;
    }

    public ExerciseSet getSet(int i)
    {
        return mExerciseSets.get(i);
    }
    /// GETTER END

    /**
     * Empty set constructor taking in factory for subtype, abstraction
     */
    public void addEmptySet()
    {
        ExerciseSet newSet = mExerciseSetFactory.get();
        newSet.setID(mExerciseSets.size() + 1);
        mExerciseSets.add(newSet);
    }
}

