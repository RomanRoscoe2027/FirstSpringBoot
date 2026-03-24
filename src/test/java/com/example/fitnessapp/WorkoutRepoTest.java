package com.example.fitnessapp;

import com.example.fitnessapp.model.*;
import com.example.fitnessapp.repository.UserRepository;
import com.example.fitnessapp.repository.WorkoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest // creates a test db for us, and automatically configures the repo beans, IN MEMORY DB
class WorkoutRepoTest
{
    @Autowired // injects the workout repo bean into the test, does the same with user beans
    private WorkoutRepository workoutRepository;

    @Autowired
    private UserRepository userRepository;

    // Create test users and workouts
    private User testUser;
    private User otherUser;

    // Create test workouts
    private Workout pushWorkout;
    private Workout pullWorkout;
    private Workout legsWorkout;
    private Workout otherUsersPushWorkout;

    @BeforeEach // runs before each test, configuring a new repo
    void setUp()
    {
        // Create valid users first
        testUser = createAndSaveUser(
                "testUser",
                "test@example.com",
                "hashed_password"
        );

        otherUser = createAndSaveUser(
                "otherUser",
                "other@example.com",
                "other_hashed_password"
        );

        // Create valid workouts for primary user
        pushWorkout = createWorkoutForUser("Push", LocalDate.of(2026, 3, 1), testUser);
        pullWorkout = createWorkoutForUser("Pull", LocalDate.of(2026, 3, 3), testUser);
        legsWorkout = createWorkoutForUser("Legs", LocalDate.of(2026, 3, 5), testUser);

        // Create a workout for a different user to verify filtering works
        otherUsersPushWorkout = createWorkoutForUser("Push", LocalDate.of(2026, 3, 2), otherUser);

        workoutRepository.saveAll(List.of(
                pushWorkout,
                pullWorkout,
                legsWorkout,
                otherUsersPushWorkout
        ));
        // save and flush does a saveAllAndFlush which saves all the workouts in one go
    }

    /**
     * Helper method to create and save a user with the given details.
     */
    private User createAndSaveUser(String username, String email, String passwordHash)
    {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        return userRepository.save(user);
    }

    /**
     * Helper method to create and save a workout for a user with the given details.
     */
    private Workout createWorkoutForUser(String dayName, LocalDate date, User user)
    {
        Workout workout = new Workout(dayName, date);
        workout.setUser(user);
        return workout;
    }

    /**
     * Test that saving a workout assigns an ID to it.
     */
    @Test
    void saveWorkoutAssignsIdTest()
    {
        Workout workout = createWorkoutForUser(
                "Upper",
                LocalDate.of(2026, 3, 7),
                testUser
        );

        Workout savedWorkout = workoutRepository.save(workout);

        assertNotNull(savedWorkout.getId());
        assertEquals("Upper", savedWorkout.getDayName());
        assertEquals(LocalDate.of(2026, 3, 7), savedWorkout.getDate());
        assertEquals(testUser.getId(), savedWorkout.getUser().getId());
    }

    /**
     * Test that findByUserId returns only workouts for the specified user.
     */
    @Test
    void findByUserIdReturnsOnlyThatUsersWorkoutsTest()
    {
        List<Workout> results = workoutRepository.findByUserId(testUser.getId());

        assertEquals(3, results.size());
        assertTrue(results.contains(pushWorkout));
        assertTrue(results.contains(pullWorkout));
        assertTrue(results.contains(legsWorkout));
        assertFalse(results.contains(otherUsersPushWorkout));
    }

    /**
     * Test that findByUserId returns only workouts for the specified user.
     */
    @Test
    void findByUserIdReturnsEmptyForUnknownUserTest()
    {
        List<Workout> results = workoutRepository.findByUserId(999999L);

        assertTrue(results.isEmpty());
    }

    /**
     * Test that findByUserIdAndDateRange returns only workouts for the specified user within the given date range.
     */
    @Test
    void findByUserIdAndDateRangeReturnsOnlyMatchingDatesForThatUserTest()
    {
        List<Workout> results = workoutRepository.findByUserIdAndDateRange(
                testUser.getId(),
                LocalDate.of(2026, 3, 2),
                LocalDate.of(2026, 3, 5)
        );

        assertEquals(2, results.size());
        assertTrue(results.contains(pullWorkout));
        assertTrue(results.contains(legsWorkout));
        assertFalse(results.contains(pushWorkout));
        assertFalse(results.contains(otherUsersPushWorkout));
    }

    /**
     * Test that findByUserIdAndDateRange returns only workouts for the specified user within the given date range.
     */
    @Test
    void findByUserIdAndDateRangeReturnsEmptyWhenNothingMatchesTest()
    {
        List<Workout> results = workoutRepository.findByUserIdAndDateRange(
                testUser.getId(),
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 30)
        );

        assertTrue(results.isEmpty());
    }

    /**
     * Test that findByUserIdAndDayName returns only workouts for the specified user on the specified day.
     */
    @Test
    void findByUserIdAndDayNameReturnsOnlyMatchingWorkoutForThatUserTest()
    {
        List<Workout> results = workoutRepository.findByUserIdAndDayName(
                testUser.getId(),
                "Push"
        );

        assertEquals(1, results.size());
        assertEquals("Push", results.get(0).getDayName());
        assertEquals(testUser.getId(), results.get(0).getUser().getId());
        assertEquals(pushWorkout.getDate(), results.get(0).getDate());
    }

    /**
     * Test that findByUserIdAndDayName returns only workouts for the specified user on the specified day.
     */
    @Test
    void findByUserIdAndDayNameDoesNotLeakOtherUsersWorkoutTest()
    {
        List<Workout> results = workoutRepository.findByUserIdAndDayName(
                otherUser.getId(),
                "Push"
        );

        assertEquals(1, results.size());
        assertEquals(otherUser.getId(), results.get(0).getUser().getId());
        assertEquals(LocalDate.of(2026, 3, 2), results.get(0).getDate());
    }

    /**
     * Test that findByUserIdAndDayName returns only workouts for the specified user on the specified day.
     */
    @Test
    void findByUserIdAndDayNameReturnsEmptyWhenNoMatchTest()
    {
        List<Workout> results = workoutRepository.findByUserIdAndDayName(
                testUser.getId(),
                "Chest"
        );

        assertTrue(results.isEmpty());
    }
}

/*
 Much simpler thatn the workout service test. Just needs to use @DataJpaTest and @Autowired to inject the repo beans,
 and create test users and workouts.
 Then we can test the repo by ensuring basic workout information and functionality is correctly
 saved, displayed, etc.
 */