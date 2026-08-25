package com.example.fitnessapp.user_workout;

import com.example.fitnessapp.workout_exercise.WorkoutExercise;
import com.example.fitnessapp.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

/**
 * Class Workout holds information regarding a specific workout.
 * Each workout is tied to a user, and one user can have multiple workouts. (User class)
 * Each workout holds exercises which are themselves tied to a workout. (Exercise class)
 */
@Entity
@Data
@Table(name = "workouts")
public class UserWorkout
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "dayName", nullable = false)
    private String dayName;

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<WorkoutExercise> exercisesInWorkout = new ArrayList<>();

    @Setter
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    public UserWorkout() {}
}


