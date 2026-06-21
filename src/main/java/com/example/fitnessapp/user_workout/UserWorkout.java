package com.example.fitnessapp.user_workout;

import com.example.fitnessapp.workout_exercise.WorkoutExercise;
import com.example.fitnessapp.user.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList; // the concrete class implementation
import java.util.List; // just an interface providing functionality via, add, size, remove, iterators, etc. Abstraction
import java.time.LocalDate; // allows for grabbing the current date as of right now

/**
 * Class Workout holds information regarding a specific workout.
 * Each workout is tied to a user, and one user can have multiple workouts. (User class)
 * Each workout holds exercises which are themselves tied to a workout. (Exercise class)
 */
@Entity
@Getter
@Table(name = "workouts")
public class UserWorkout
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Setter
    @Column(name = "dayName", nullable = false)
    private String dayName;

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<WorkoutExercise> exercisesInWorkout = new ArrayList<>();

    @Setter
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /// Default constructor for JPA
    public UserWorkout(String dayName, LocalDate date) {
        this.dayName = dayName;
        this.date = date;
    }

    /**
     * Adds an exercise to the workout.
     * @param exercise
     */
    public void addExercise(WorkoutExercise exercise)
    {
        exercisesInWorkout.add(exercise);
        exercise.setWorkout(this);
    }

}


