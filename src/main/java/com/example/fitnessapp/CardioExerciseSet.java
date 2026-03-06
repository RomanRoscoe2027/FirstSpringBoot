package com.example.fitnessapp;
import java.time.Duration;
/**
 * Cardio set derived from class set.
 *
 * For now this is enough info. Overrides display from base set class
 *
 * Contains, member variables, duration, distance, etc.
 */
public class CardioExerciseSet extends ExerciseSet {

    /// uses same time package previously for DateTime of workout
    private Duration mDuration;

    /// minutes
    private double mDistance;

    /**
     * Constructor for a CardioExerciseSet
     * @param reps
     * @param duration
     * @param distance
     */
    public CardioExerciseSet(int id, int reps, Duration duration, double distance) {
        super(id, reps);

        this.mDuration = duration;
        this.mDistance = distance;
    }

    /**
     * Empty Constructor
     */
    public CardioExerciseSet() {
        this.mDuration = Duration.ofMinutes(0);
        this.mDistance = 0;
    }
    /// GETTERS
    public Duration getDuration() {
        return mDuration;
    }

    public double getDistance() {
        return mDistance;
    }

    /// SETTERS
    public void setDuration(Duration mDuration) {
        this.mDuration = mDuration;
    }

    public void setDistance(double mDistance) {
        this.mDistance = mDistance;
    }

    /**
     * Override abstract display func for different string formatting.
     * No need for SB
     * @return string object for display
     */
    @Override
    public String display() {
        return "Cardio Set " + getID() +
                ": " + getReps() +
                " rep(s)." +
                " Distance " + mDistance +
                " miles in " + mDuration.toMinutes() + " minutes.";
    }
}
