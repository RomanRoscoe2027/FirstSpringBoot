package com.example.fitnessapp;

import com.example.fitnessapp.model.Workout;
import com.example.fitnessapp.repository.WorkoutRepository;
import java.time.LocalDate;
import java.util.List;

// JUnit annotation to run setup code before each test method
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Spring annotation to inject dependencies automatically
import org.springframework.beans.factory.annotation.Autowired;
// Configures MockMvc for testing web layer without starting a full HTTP server
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// Loads the complete Spring application context for integration testing
import org.springframework.boot.test.context.SpringBootTest;
// MockMvc is the main entry point for server-side Spring MVC testing
import org.springframework.test.web.servlet.MockMvc;
// MvcResult captures the response from MockMvc for manual assertions
import org.springframework.test.web.servlet.MvcResult;


// JsonPath library to parse and extract values from JSON responses
import com.jayway.jsonpath.JsonPath;
// JUnit assertion methods for verifying test conditions
import static org.junit.jupiter.api.Assertions.*;

// Builder for DELETE HTTP requests in tests
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
// Builder for GET HTTP requests in tests
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// Builder for POST HTTP requests in tests
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// Matcher to verify HTTP response status codes (200, 404, 400, etc.)
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Tells Spring to load the full application context for integration testing
@SpringBootTest
// Automatically configures MockMvc for testing controllers without starting a real server
@AutoConfigureMockMvc
public class WorkoutControllerTest
{
    // Injects MockMvc to simulate HTTP requests to the controller
    @Autowired
    private MockMvc mockMvc;

    // Injects the repository to set up test data before each test
    @Autowired
    private WorkoutRepository repository;

    // Runs before each test method to ensure a clean database state
    @BeforeEach
    void setUp()
    {
        for (Workout workout : repository.findAll())
        {
            repository.delete(workout);
        }
    }

    // Tests that GET /api/workouts returns all workouts as a JSON array
    @Test
    void getAllWorkoutsReturnsListTest() throws Exception
    {
        repository.save(new Workout("Push", LocalDate.of(2026, 3, 1)));
        repository.save(new Workout("Pull", LocalDate.of(2026, 3, 3)));

        MvcResult result = mockMvc.perform(get("/api/workouts"))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        List<String> names = JsonPath.read(json, "$[*].dayName");

        assertEquals(2, names.size());
        assertTrue(names.contains("Push"));
        assertTrue(names.contains("Pull"));
    }

    // Tests that POST /api/workouts with name and date creates and returns a workout
    @Test
    void createWorkoutWithDateReturnsWorkoutTest() throws Exception
    {
        MvcResult result = mockMvc.perform(post("/api/workouts")
                        .param("name", "Push")
                        .param("date", "2026-03-01"))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        String dayName = JsonPath.read(json, "$.dayName");
        String date = JsonPath.read(json, "$.date");

        assertEquals("Push", dayName);
        assertEquals("2026-03-01", date);
    }

    // Tests that POST /api/workouts with only name uses current date automatically
    @Test
    void createWorkoutWithoutDateReturnsWorkoutTest() throws Exception
    {
        MvcResult result = mockMvc.perform(post("/api/workouts")
                        .param("name", "Push"))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        String dayName = JsonPath.read(json, "$.dayName");
        String date = JsonPath.read(json, "$.date");

        assertEquals("Push", dayName);
        assertNotNull(date);
    }

    // Tests that GET /api/workouts/search/name filters workouts by name parameter
    @Test
    void searchByNameReturnsMatchesTest() throws Exception
    {
        repository.save(new Workout("Push", LocalDate.of(2026, 3, 1)));
        repository.save(new Workout("Pull", LocalDate.of(2026, 3, 3)));

        MvcResult result = mockMvc.perform(get("/api/workouts/search/name")
                        .param("value", "Push"))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        List<String> names = JsonPath.read(json, "$[*].dayName");

        assertEquals(1, names.size());
        assertEquals("Push", names.get(0));
    }

    // Tests that GET /api/workouts/search/date filters workouts within date range
    @Test
    void searchByDateReturnsMatchesTest() throws Exception
    {
        repository.save(new Workout("Push", LocalDate.of(2026, 3, 1)));
        repository.save(new Workout("Pull", LocalDate.of(2026, 3, 3)));
        repository.save(new Workout("Legs", LocalDate.of(2026, 3, 5)));

        MvcResult result = mockMvc.perform(get("/api/workouts/search/date")
                        .param("start", "2026-03-02")
                        .param("end", "2026-03-05"))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        List<String> names = JsonPath.read(json, "$[*].dayName");

        assertEquals(2, names.size());
        assertTrue(names.contains("Pull"));
        assertTrue(names.contains("Legs"));
    }

    // Tests that DELETE /api/workouts removes and returns the deleted workout
    @Test
    void deleteWorkoutReturnsWorkoutTest() throws Exception
    {
        repository.save(new Workout("Push", LocalDate.of(2026, 3, 1)));

        MvcResult result = mockMvc.perform(delete("/api/workouts")
                        .param("name", "Push")
                        .param("date", "2026-03-01"))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        String dayName = JsonPath.read(json, "$.dayName");

        assertEquals("Push", dayName);
    }

    // Tests that DELETE /api/workouts returns 404 when workout doesn't exist
    @Test
    void deleteWorkoutNotFoundReturns404Test() throws Exception
    {
        mockMvc.perform(delete("/api/workouts")
                        .param("name", "Push")
                        .param("date", "2026-03-01"))
                .andExpect(status().isNotFound());
    }

    // Tests that POST /api/workouts without required name parameter returns 400
    @Test
    void createWorkoutMissingNameReturns400Test() throws Exception
    {
        mockMvc.perform(post("/api/workouts")
                        .param("date", "2026-03-01"))
                .andExpect(status().isBadRequest());
    }

    // Tests that GET /api/workouts/search/date with invalid date format returns 400
    @Test
    void searchByDateInvalidFormatReturns400Test() throws Exception
    {
        mockMvc.perform(get("/api/workouts/search/date")
                        .param("start", "2026-13-01")
                        .param("end", "2026-03-05"))
                .andExpect(status().isBadRequest());
    }
}
