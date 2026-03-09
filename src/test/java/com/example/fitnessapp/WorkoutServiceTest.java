package com.example.fitnessapp;

import com.example.fitnessapp.model.Workout;
import com.example.fitnessapp.repository.WorkoutRepository;
import com.example.fitnessapp.service.WorkoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class WorkoutServiceTest
{
    private WorkoutRepository repository;
    private WorkoutService service;

    @BeforeEach
    void setUp()
    {
        repository = new WorkoutRepository();
        service = new WorkoutService(repository);
    }

    @Test
    void addWorkoutTrimsNameAndSavesTest()
    {
        LocalDate date = LocalDate.of(2026, 3, 1);

        Workout workout = service.addWorkout("  Push  ", date);

        assertEquals("Push", workout.getDayName());
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void addWorkoutBlankNameThrowsTest()
    {
        assertThrows(IllegalArgumentException.class, () -> service.addWorkout(" ", LocalDate.now()));
    }

    @Test
    void findWorkoutsMatchingDateInvalidRangeThrowsTest()
    {
        LocalDate start = LocalDate.of(2026, 3, 5);
        LocalDate end = LocalDate.of(2026, 3, 1);

        assertThrows(IllegalArgumentException.class, () -> service.findWorkoutsMatchingDate(start, end));
    }

    @Test
    void removeWorkoutReturnsWorkoutAndDeletesTest()
    {
        LocalDate date = LocalDate.of(2026, 3, 1);
        repository.save(new Workout("Push", date));

        Optional<Workout> removed = service.removeWorkout("push", date);

        assertTrue(removed.isPresent());
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void removeWorkoutNullDateThrowsTest()
    {
        assertThrows(IllegalArgumentException.class, () -> service.removeWorkout("Push", null));
    }

    @Test
    void findWorkoutsMatchingNameBlankThrowsTest()
    {
        assertThrows(IllegalArgumentException.class, () -> service.findWorkoutsMatchingName("  "));
    }

    @Test
    void findWorkoutsMatchingDateNullsThrowsTest()
    {
        assertThrows(IllegalArgumentException.class, () -> service.findWorkoutsMatchingDate(null, LocalDate.now()));
        assertThrows(IllegalArgumentException.class, () -> service.findWorkoutsMatchingDate(LocalDate.now(), null));
    }
}
