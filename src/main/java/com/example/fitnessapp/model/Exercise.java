package com.example.fitnessapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Describes an Exercise. Holds all sets via an array.
 */
@Entity
@Getter
@Table(name = "exercises")
public class Exercise {
    public enum SetKind {
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

    /**
     * @OneToMany annotation establishes a one-to-many relationship between Exercise and ExerciseSet entities.
     * @CascadeType.ALL ensures that all operations (persist, merge, remove) are cascaded to the associated ExerciseSet entities and
     * effect that entity as well and not just this one.
     * @OrphanRemoval=true allows JPA to automatically remove orphaned ExerciseSet entities when they are no longer associated with an Exercise
     */
    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ExerciseSet> exerciseSets = new ArrayList<>(); // owns many exercise sets.

    /**
     * Key classifier differentiating exercise set types.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "set_kind", nullable = false)
    private SetKind setKind;


    /**
     * -- SETTER --
     *  Tells the workout that this exercise belongs to it. Many to one relationship.
     *  Called from workout class upon creation.
     *
     * @param workout
     */
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    // not needed, but prevents loading exercises for workout until db gets query explicitly
    @JoinColumn(name = "workout_id", nullable = false) // foreign key to workout
    @JsonBackReference
    private Workout workout; // exercise owned by this workout, back references via json format without breaking


    /// Default constructor for JPA
    public Exercise() {

    }

    /**
     * Constructor given a name, number of sets, and subtype of set, cardio or lifting.
     *
     * @param name
     * @param numberOfSets
     * @param setKind
     */
    public Exercise(String name, Integer numberOfSets, SetKind setKind) {
        /// Name of exercise
        this.name = name;
        this.setKind = setKind;
        for (int i = 0; i < numberOfSets; i++) {
            addEmptySet();
        }
    }

    /**
     * Creates an Exercise that uses LiftingExerciseSet objects.
     *
     * @param name
     * @param numberOfSets
     * @return LiftingExerciseSet
     */
    public static Exercise makeLiftingExercise(String name, Integer numberOfSets) {
        return new Exercise(name, numberOfSets, SetKind.LIFTING);
    }

    /**
     * Creates an Exercise that uses CardioExerciseSet objects.
     *
     * @param name
     * @param numberOfSets
     * @return
     */
    public static Exercise makeCardioExercise(String name, Integer numberOfSets) {
        return new Exercise(name, numberOfSets, SetKind.CARDIO);
    }

    /**
     * Empty set constructor taking in enumeration type for classification of set.
     * Calls addSet() to add a new set to the exercise, that gives ownership to the exercise and tells
     * set what exercise it belongs to.
     */
    public void addEmptySet() {
        ExerciseSet newSet = createSetByKind();
        newSet.setSetNumber(exerciseSets.size() + 1);
        addSet(newSet);
    }

    /**
     * Create a set based off of the enum setKind classified for the given set.
     *
     * @return
     */
    private ExerciseSet createSetByKind() {
        return switch (setKind) {
            case LIFTING -> new LiftingExerciseSet();
            case CARDIO -> new CardioExerciseSet();
        };
    }

    /**
     * Adds a set to the exercise. Sets exercise to this exercise.
     * Ensures that the set is of the correct type for this exercise, throws exception if not.
     *
     * @param set - ExerciseSet to add to the exercise
     */
    public void addSet(ExerciseSet set) {
        boolean valid = switch (setKind) {
            case LIFTING -> set instanceof LiftingExerciseSet;
            case CARDIO -> set instanceof CardioExerciseSet;
        };

        if (!valid) {
            throw new IllegalArgumentException("Set type does not match exercise kind " + setKind);
        }

        exerciseSets.add(set);
        set.setExercise(this);
    }

    /**
     * Gets a set from the exercise by index. Throws exception if index is out of bounds.
     * @param index
     * @return
     */
    public ExerciseSet getSet(int index) {
        Objects.checkIndex(index, exerciseSets.size());
        return exerciseSets.get(index);
    }
}


