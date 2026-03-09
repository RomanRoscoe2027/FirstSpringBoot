package com.example.fitnessapp;

import com.example.fitnessapp.model.Workout;
import com.example.fitnessapp.repository.WorkoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WorkoutControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorkoutRepository repository;

    @BeforeEach
    void setUp()
    {
        for (Workout workout : repository.findAll())
        {
            repository.delete(workout);
        }
    }

    @Test
    void getAllWorkoutsReturnsListTest() throws Exception
    {
        repository.save(new Workout("Push", LocalDate.of(2026, 3, 1)));
        repository.save(new Workout("Pull", LocalDate.of(2026, 3, 3)));

        mockMvc.perform(get("/api/workouts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].dayName", containsInAnyOrder("Push", "Pull")));
    }

    @Test
    void createWorkoutWithDateReturnsWorkoutTest() throws Exception
    {
        mockMvc.perform(post("/api/workouts")
                        .param("name", "Push")
                        .param("date", "2026-03-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dayName").value("Push"))
                .andExpect(jsonPath("$.date").value("2026-03-01"));
    }

    @Test
    void createWorkoutWithoutDateReturnsWorkoutTest() throws Exception
    {
        mockMvc.perform(post("/api/workouts")
                        .param("name", "Push"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dayName").value("Push"))
                .andExpect(jsonPath("$.date").value(notNullValue()));
    }

    @Test
    void searchByNameReturnsMatchesTest() throws Exception
    {
        repository.save(new Workout("Push", LocalDate.of(2026, 3, 1)));
        repository.save(new Workout("Pull", LocalDate.of(2026, 3, 3)));

        mockMvc.perform(get("/api/workouts/search/name")
                        .param("value", "Push"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].dayName").value("Push"));
    }

    @Test
    void searchByDateReturnsMatchesTest() throws Exception
    {
        repository.save(new Workout("Push", LocalDate.of(2026, 3, 1)));
        repository.save(new Workout("Pull", LocalDate.of(2026, 3, 3)));
        repository.save(new Workout("Legs", LocalDate.of(2026, 3, 5)));

        mockMvc.perform(get("/api/workouts/search/date")
                        .param("start", "2026-03-02")
                        .param("end", "2026-03-05"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].dayName", containsInAnyOrder("Pull", "Legs")));
    }

    @Test
    void deleteWorkoutReturnsWorkoutTest() throws Exception
    {
        repository.save(new Workout("Push", LocalDate.of(2026, 3, 1)));

        mockMvc.perform(delete("/api/workouts")
                        .param("name", "Push")
                        .param("date", "2026-03-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dayName").value("Push"));
    }

    @Test
    void deleteWorkoutNotFoundReturns404Test() throws Exception
    {
        mockMvc.perform(delete("/api/workouts")
                        .param("name", "Push")
                        .param("date", "2026-03-01"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createWorkoutMissingNameReturns400Test() throws Exception
    {
        mockMvc.perform(post("/api/workouts")
                        .param("date", "2026-03-01"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void searchByDateInvalidFormatReturns400Test() throws Exception
    {
        mockMvc.perform(get("/api/workouts/search/date")
                        .param("start", "2026-13-01")
                        .param("end", "2026-03-05"))
                .andExpect(status().isBadRequest());
    }
}
