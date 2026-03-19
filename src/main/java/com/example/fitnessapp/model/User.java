package com.example.fitnessapp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * This class describes a user holding an array list full of workouts.
 * Also contains various important variables and pieces of information regarding the user such as
 * the user's weight, name, username, email, password, etc.
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
    private Double height;

    @Setter
    private Double bodyWeight;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Workout> workouts = new ArrayList<>();

    public User() {}

    public void addWorkout(Workout workout) {
        workouts.add(workout);
        workout.setUser(this);
    }

    public void removeWorkout(Workout workout) {
        workouts.remove(workout);
        workout.setUser(null);
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}