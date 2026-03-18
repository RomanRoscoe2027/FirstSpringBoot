package com.example.fitnessapp.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Describes an Exercise. Holds all sets via an array.
 */
@Entity
@Getter
@Table(name = "exercises")
public class Exercise
{
    public enum SetKind
    {
        LIFTING,
        CARDIO
    }

    /**
     * Unique identifier for the exercise. Auto incremented by DB dont worry about from model.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "name")
    private String name;
    /// Dynamic array containing all sets for an exercise

    /**
     * @OneToMany annotation establishes a one-to-many relationship between Exercise and ExerciseSet entities.
     * @JoinColumn specifies the foreign key column in the ExerciseSet table that references the Exercise table.
     * This being exercise_id of course.
     * @CascadeType.ALL ensures that all operations (persist, merge, remove) are cascaded to the associated ExerciseSet entities and
     * effect that entity as well and not just this one.
     * @OrphanRemoval=true allows JPA to automatically remove orphaned ExerciseSet entities when they are no longer associated with an Exercise
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "exercise_id")
    private List<ExerciseSet> exerciseSets;

    /**
     * Far easier and far more simple than fancy supplier handling with extensions.
     * Key classifier differentiating exercise set types.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "set_kind", nullable = false)
    private SetKind setKind;

    /// Default constructor for JPA
    public Exercise() {
        this.exerciseSets = new ArrayList<>();
    }

    /**
     * Constructor given a name, number of sets, and subtype of set, cardio or lifting.
     * @param name
     * @param numberOfSets
     * @param setKind
     */
    public Exercise(String name, int numberOfSets, SetKind setKind)
    {
        /// Name of exercise
        this.name = name;

        /// Set array
        this.exerciseSets = new ArrayList<>();
        this.setKind = setKind;
        /// Given user set number, create a set where we allocate amount of set objects to default 0's
        /// Later user will fill in given information for sets
        for (int i = 0; i < numberOfSets; i++)
        {
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
        return new Exercise(name, numberOfSets, SetKind.LIFTING);
    }

    /**
     * Creates an Exercise that uses CardioExerciseSet objects.
     * @param name
     * @param numberOfSets
     * @return
     */
    public static Exercise makeCardioExercise(String name, int numberOfSets)
    {
        return new Exercise(name, numberOfSets, SetKind.CARDIO);
    }

    /**
     * Empty set constructor taking in factory for subtype, abstraction
     */
    public void addEmptySet()
    {
        ExerciseSet newSet = createSetByKind();
        newSet.setID(exerciseSets.size() + 1);
        exerciseSets.add(newSet);
    }

    public ExerciseSet getSet(int index)
    {
        Objects.checkIndex(index, exerciseSets.size());
        return exerciseSets.get(index);
    }

    private ExerciseSet createSetByKind()
    {
        if (setKind == null)
        {
            throw new IllegalStateException("Exercise set kind must be set before adding sets.");
        }

        return switch (setKind)
        {
            case LIFTING -> new LiftingExerciseSet();
            case CARDIO -> new CardioExerciseSet();
        };
    }
}
