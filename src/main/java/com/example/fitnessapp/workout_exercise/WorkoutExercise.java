package com.example.fitnessapp.workout_exercise;

import com.example.fitnessapp.exercise_set.ExerciseSet;
import com.example.fitnessapp.user_workout.UserWorkout;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes an Exercise. Holds all sets via an array.
 */
@Entity
@Getter
@Table(name = "exercises")
public class WorkoutExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "numberOfSets")
    private Integer numberOfSets;

    @Setter
    @ManyToOne
    @JoinColumn(name = "workout_id", nullable = false)
    @JsonBackReference
    private UserWorkout workout;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ExerciseSet> exerciseSets = new ArrayList<>();

    /**
     * Default constructor for JPA.
     * @param id
     * @param name
     * @param numberOfSets
     */
    public WorkoutExercise(Long id, String name, Integer numberOfSets) {
        this.id = id;
        this.name = name;
        this.numberOfSets = numberOfSets;
    }
}


