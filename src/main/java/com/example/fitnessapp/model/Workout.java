package com.example.fitnessapp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList; // the concrete class implementation
import java.util.List; // just an interface providing functionality via, add, size, remove, iterators, etc. Abstraction
import java.time.LocalDate; // allows for grabbing the current date as of right now

/**
 * Class Workout holds information regarding a specific workout such as:
 * mId = the indicator of each workout for differentiating workouts
 * mDayName = the name of the workout in question, could be split, or simply the day
 * mExcercises = a list that holds the collection of excercises for a workout. Each of object excercise
 *
 * Will hopefully delete default constructor one day when I figure out if there is a
 * point to do so in Java like C++.
 *
 * This workout class is still under development.
 *
 * Probably could include setters at some point as well
 */
@Entity
@Getter // don't need to complicate and extend code with manual getting when can use lombok lol. how cool
@Table(name = "workouts")
public class Workout
{
    /**
     * @ID and @GeneratedValue apply directly to private id field.
     * When a new workout is created DB generates the PK value and JPA sets it back on the entity.
     * Therefor I should no longer pas id in constructors for new objects.
     *
     * Should now create a no arg constructor for JPA because it will be auto-generated.
     */
    @Id // primary key identifier
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment primary key
    @Column(name = "id") // keep existing DB column name
    private Long id;

    @Setter
    @Column(name = "dayName", nullable = false)
    private String dayName;
    // all workouts have a name like push, pull, legs etc

    /**
     * @OneToMany annotation establishes a one-to-many relationship between Workout and Exercise entities.
     *
     * Mapped by is better than @JoinColumn here as it gives direct ownership needed by exercise to a workout, rather
     * thatn a exercise being able to belong independently. Fits bidirectional relationship better. Same idea for
     * Exercise ----> exerciseSets.
     *
     * @CascadeType.ALL ensures that all operations (persist, merge, remove) are cascaded to the associated Exercise entities and
     * effect that entity as well and not just this one.
     * @OrphanRemoval=true allows JPA to automatically remove orphaned Exercise entities when they are no longer associated with a Workout.
     * Important as exercises would not belong to any workout if removed and have no reason to stay in DB
     */
    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // allows for json serialization of workout, without infinite reference recursion as workout owns exercises which backtrack to workouts
    private List<Exercise> exercises = new ArrayList<>();
    // Have each workout hold a list containing all exercises of object type exercise from exercise class
    // array format for now, but lets us have some flexibility for dev if want to switch

    @Column(name = "date", nullable = false)
    private LocalDate date;
    // typical way to standardize time via java.time package. Will use often

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // workout must be owned by a user

    /// Default constructor for JPA
    public Workout()
    {

    }


    /// Constructor given name, let tracker handle ids
    public Workout(String name)
    {
        this.dayName = name;
        this.date = LocalDate.now();
    }

    /// Constructor allowing date setting (good for junit testing)
    public Workout(String name, LocalDate date)
    {
        this.dayName = name;
        this.date = date;
    }

    /**
     * Functionality to display workout, but largely depends on individual display funcs
     * via various exercise set types, will be easy to add on with polymorphism if need be.
     *
     * Utilizes StringBuilder ...
     * edit: very cool, Strings are immutable, string builder allows easy and fast rapid growth to a string
     * like object, just need to transform to a string upon returning.
     *
     * @return sb.toString(): Essentially Large paragraph like string containing workout information
     */
    public String displayWorkout() {
        StringBuilder sb = new StringBuilder();
        // create StringBuilder object to continuously add too
        sb.append("Workout Date: ").append(date).append("\n");
        sb.append("----------------------------\n");
        // can literally just rapidly append to, NOT an actual string... weird stuff
        for (Exercise exercise : exercises)
        // sift through all exercises and return only necessary info via getters
        {
            sb.append("Exercise: ").append(exercise.getName()).append("\n");
            Integer setNumber = 1;
            for (ExerciseSet exerciseSet : exercise.getExerciseSets())
            {
                        sb.append(exerciseSet.display()).append("\n");
                setNumber++;
            }

            sb.append("\n");
        }
        // and because not string, must fully change to string via convienent toString()
        return sb.toString();
    }

    /**
     * Replaced Log exercise with this, same idea, more concrete naming and professional.
     * @param exercise
     */
    public void addExercise(Exercise exercise)
    {
        exercises.add(exercise);
        exercise.setWorkout(this); // tells exercise which workout it belongs too
    }
}


