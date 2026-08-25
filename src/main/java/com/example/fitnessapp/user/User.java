package com.example.fitnessapp.user;

import com.example.fitnessapp.user_workout.UserWorkout;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

/**
 * This class describes a user holding an array list full of workouts.
 * Each user is held and stored in our db, tied to each workout, stored in a different table.
 * They are tied by ID.
 */
@Entity
@Data
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<UserWorkout> userWorkouts;

    public User() {}
}