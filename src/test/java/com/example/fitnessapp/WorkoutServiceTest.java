package com.example.fitnessapp;

import com.example.fitnessapp.dto.request.CreateWorkoutRequest;
import com.example.fitnessapp.model.User;
import com.example.fitnessapp.model.Workout;
import com.example.fitnessapp.repository.UserRepository;
import com.example.fitnessapp.repository.WorkoutRepository;
import com.example.fitnessapp.service.WorkoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test the workout service.
 * Should mainly test the interaction with the repositories, not actual database logic. That will go in repo test.
 * Also tests the service itself, which is a good thing, and ensures errorhandling.
 *
 * Some tests ensure that the user repo is linked to workout repo and service functionality as well,
 * especially upon creation of workout.
 *
 * READ COMMENTS IF YOU ARE NOT SURE WHAT A TEST DOES
 */
@ExtendWith(MockitoExtension.class) // allows mocking of repositories, allows for combination of junit and mockito
public class WorkoutServiceTest
{
    @Mock /// Mocks the repository to avoid database access during tests
    private UserRepository userRepository; // remember, NEED user repo to create a workout, hence mock needs it as well
    @Mock
    private WorkoutRepository workoutRepository; // mocking the repo is enough to avoid database access

    private WorkoutService workoutService;

    @BeforeEach /// runs before each test, configuring a new service @BeforeALl, would share resources, this resets before each test
    void setUp()
    {
        workoutService = new WorkoutService(workoutRepository, userRepository);
        // now service can be tested because it has both mock repos
    }

    ///  WORKOUT CREATION TEST MOST IMPORTANT, CORRELATES TO BOTH REPOS
    @Test // duh just a test
    void createWorkoutTrimsNameAndSavesTest()
    {
        // for create request and id creation/lookups
        Long userId = 1L;
        LocalDate date = LocalDate.of(2026, 3, 1);

        // Create a mock user to satisfy the userRepository.findById() call
        User mockUser = new User(); // an empty user
        mockUser.setUsername("testUser"); // now has a name
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Initialize the DTO required by the service
        CreateWorkoutRequest request = new CreateWorkoutRequest();
        request.setDayName("  Push  "); // set with whitespace for trim catch
        request.setDate(date); // set date

        // Prepare the expected saved entity
        Workout savedWorkout = new Workout("Push", date);
        savedWorkout.setUser(mockUser); // associate the workout with the mock user
        when(workoutRepository.save(any(Workout.class))).thenReturn(savedWorkout); //

        // 2. Act
        Workout result = workoutService.createWorkout(userId, request);
        // HERE should call both userRepo.find and workoutRepo.save

        // 3. Assert
        assertNotNull(result); // ok so it saved?
        assertEquals("Push", result.getDayName(), "The workout name should be trimmed before saving.");
        assertEquals(mockUser, result.getUser(), "The workout should be associated with the correct user.");

        // Verify that the service interacted with the repositories as expected
        verify(userRepository).findById(userId);
        verify(workoutRepository).save(any(Workout.class));

        /*
        UNDERSTANDING WHY THIS WORKS:
        Accounts for User lookup which is necessary for any creation of a workout.
        Ensures that the workout is associated with the correct user.
        Verifies that the workout repository is called with the expected workout object.

        Basically,
        when(workoutRepository.save(any(Workout.class))).thenReturn(savedWorkout)
        This is the vital part, basically describes that whenever the repo is called to save an object
        as long as it is of the workout class, then return the saved workout already created
        In other words, when the repo is pinged to save, return our predefined right answer

        Same idea for the user portion, instead we create an empty user, set its name
        and then when pinged by the service it returns that user object which is compared.

        SO the logic of the repo itself isn't tested, but the service to repo calls are confirmed
        to be connecting and workout. Verify ensures that the service is correctly interacting with the repository

        Our function test defines workout traits needed for creation, the createWorkout func is called
        which then calls both workoutRepo.save and userRepo.find both with our predefined
        return answers, which we assert upon to ensure logic and integrity of returns

        verify just ensures interactions happened, although that should be obvious from assertions as well
         */
    }

    ///  MATCHING DATE INTERVAL TEST ENSURES ERRORS
    @Test
    void findWorkoutsMatchingDateInvalidRangeThrowsTest() {
        // define some fd up date ordering
        Long userId = 1L;
        LocalDate start = LocalDate.of(2026, 3, 5);
        LocalDate end = LocalDate.of(2026, 3, 1);

        // Ensure the service still blocks invalid date ranges with throw
        assertThrows(IllegalArgumentException.class, () ->
                workoutService.findWorkoutsMatchingDate(userId, start, end));
    }

    ///  WORKOUT REMOVAL TEST
    @Test
    void removeWorkoutReturnsWorkoutAndDeletesTest() {
        // 1. Arrange
        Long userId = 1L;
        String name = "Push";
        LocalDate date = LocalDate.of(2026, 3, 1);
        Workout workout = new Workout(name, date);

        // Mock the specific lookup the service now performs
        when(workoutRepository.findByUserIdAndDayNameAndDate(userId, name, date))
                .thenReturn(Optional.of(workout)); // return the workout or nothing if stuff is broken or unfound

        // 2. Act
        Optional<Workout> removed = workoutService.removeWorkout(userId, name, date); // call the service to remove workout

        // 3. Assert
        assertTrue(removed.isPresent()); // means workout succesfully removed and returned to us
        assertEquals(workout, removed.get());

        // 4. Verify Behavior (This replaces the old findAll() check)
        verify(workoutRepository).delete(workout); // call worked to delete the workout
    }

    @Test
    void removeWorkoutNullDateThrowsTest() {
        assertThrows(IllegalArgumentException.class, () ->
                workoutService.removeWorkout(1L, "Push", null));
    }

    @Test
    void findWorkoutsMatchingNameBlankThrowsTest() {
        assertThrows(IllegalArgumentException.class, () ->
                workoutService.findWorkoutsMatchingName(1L, "  "));
    }

    @Test
    void findWorkoutsMatchingDateNullsThrowsTest() {
        Long userId = 1L;
        assertThrows(IllegalArgumentException.class, () ->
                workoutService.findWorkoutsMatchingDate(userId, null, LocalDate.now()));
        assertThrows(IllegalArgumentException.class, () ->
                workoutService.findWorkoutsMatchingDate(userId, LocalDate.now(), null));
    }
}
