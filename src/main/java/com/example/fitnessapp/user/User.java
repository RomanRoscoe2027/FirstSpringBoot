package com.example.fitnessapp.user;

import com.example.fitnessapp.user_workout.UserWorkout;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * This class describes a user holding an array list full of workouts.
 * Each user is held and stored in our db, tied to each workout, stored in a different table. They are tied by ID.
 */
@Entity
@Getter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Setter
    @Column(nullable = false, unique = true)
    private String username;

    @Setter
    @Column(nullable = false, unique = true)
    private String email;

    @Setter
    @Column(nullable = false)
    private String passwordHash;

    @Setter
    private String firstName;

    @Setter
    private String lastName;

    @Setter
    private BigDecimal height;

    @Setter
    private BigDecimal bodyWeight;

    @Setter
    private String gender;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<UserWorkout> userWorkouts = new ArrayList<>();

    /**
     * Default constructor for JPA. Unsure about other fields for now was thinking of manually adding them later.
     *
     * @param username starting username for a user
     * @param email starting email for a user
     * @param passwordHash the hashed password for the user
     */
    public User(String username,
                String email,
                String passwordHash) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    /**
     * Adds a workout to the user's workout arrays.
     * @param workout
     */
    public void addWorkout(UserWorkout workout) {
        userWorkouts.add(workout);
        workout.setUser(this);
    }

    /**
     * Removes a workout from the user's workout arrays.
     * @param workout
     */
    public void removeWorkout(UserWorkout workout) {
        userWorkouts.remove(workout);
        workout.setUser(null);
    }
}