package com.example.fitnessapp.user_workout;

import com.example.fitnessapp.user.User;
import com.example.fitnessapp.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserWorkoutService {

    private final UserWorkoutRepository workoutRepository;
    private final UserRepository userRepository;

    public UserWorkoutService(
            UserWorkoutRepository workoutRepository,
            UserRepository userRepository) {

        this.workoutRepository = workoutRepository;
        this.userRepository = userRepository;
    }

    public UserWorkout createWorkout(Long userId, UserWorkout workout) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        workout.setUser(user);
        return workoutRepository.save(workout);
    }

    public List<UserWorkout> getWorkoutsByUser(Long userId) {
        return workoutRepository.findByUserId(userId);
    }
}