package com.mello.service;

import com.mello.model.Habit;
import com.mello.repository.HabitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitService {
    @Autowired
    private HabitRepository habitRepository;

// NOURHAN
    @Autowired
    private NotificationService notificationService;

    public List<Habit> getAllHabits(String userId) {
        return habitRepository.findByUserId(userId);
    }

    public Habit createHabit(Habit habit) {
        if (habit.getCompletedDays() == null) {
            habit.setCompletedDays(List.of(false, false, false, false, false, false, false));
        }
        if (habit.getProgress() == null) {
            habit.setProgress(0);
        }
        if (habit.getStreak() == null) {
            habit.setStreak(0);
        }
        Habit saved = habitRepository.save(habit);
        notificationService.onHabitAdded(saved);
        return saved;
    }

    public void deleteHabit(String id) {
        habitRepository.deleteById(id);
    }

    public Habit updateHabit(String id, Habit habit) {
        habit.setId(id);
        return habitRepository.save(habit);
    }
}
