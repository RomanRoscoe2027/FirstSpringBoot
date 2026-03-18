package com.example.fitnessapp.model;

import jakarta.persistence.*;
import java.time.Duration;
import lombok.Getter;
/**
 * Cardio set derived from class set.
 *
 * For now this is enough info. Overrides display from base set class
 *
 * Contains, member variables, duration, distance, etc.
 */
@Entity
@Getter
@DiscriminatorValue("CARDIO") // adds a column like set_type as a distinguisher
public class CardioExerciseSet extends ExerciseSet {

    /// uses same time package previously for DateTime of workout
    private Duration duration;

    /// minutes
    private double distance;

    /**
     * Constructor for a CardioExerciseSet
     * @param reps
     * @param duration
     * @param distance
     */
    public CardioExerciseSet(int id, int reps, Duration duration, double distance) {
        super(id, reps);

        this.duration = duration;
        this.distance = distance;
    }

    /**
     * Empty Constructor
     */
    public CardioExerciseSet() {
        this.duration = Duration.ofMinutes(0);
        this.distance = 0;
    }

    /// SETTERS
    public void setDuration(Duration mDuration) {
        this.duration = mDuration;
    }
    public void setDistance(double mDistance) {
        this.distance = mDistance;
    }

    /**
     * Override abstract display func for different string formatting.
     * No need for SB
     * @return string object for display
     */
    @Override
    public String display() {
        return "Cardio Set " + getSetNumber() +
                ": " + getReps() +
                " rep(s)." +
                " Distance " + distance +
                " miles in " + duration.toMinutes() + " minutes.";
    }
}
