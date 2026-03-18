package com.example.fitnessapp;

import com.example.fitnessapp.model.Workout;
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

@ExtendWith(MockitoExtension.class)
public class WorkoutServiceTest
{
    @Mock
    private WorkoutRepository repository;
    private WorkoutService service;

    @BeforeEach
    void setUp()
    {
        service = new WorkoutService(repository);
    }

    @Test
    void addWorkoutTrimsNameAndSavesTest()
    {
        LocalDate date = LocalDate.of(2026, 3, 1);
        Workout savedWorkout = new Workout("Push", date);

        when(repository.save(any(Workout.class))).thenReturn(savedWorkout);
        when(repository.findAll()).thenReturn(List.of(savedWorkout));

        Workout workout = service.addWorkout("  Push  ", date);

        assertEquals("Push", workout.getDayName());
        verify(repository).save(any(Workout.class));
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
        Workout workout = new Workout("Push", date);

        when(repository.findAll()).thenReturn(List.of(workout))
                                   .thenReturn(new ArrayList<>());

        Optional<Workout> removed = service.removeWorkout("push", date);

        assertTrue(removed.isPresent());
        verify(repository).delete(workout);
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
