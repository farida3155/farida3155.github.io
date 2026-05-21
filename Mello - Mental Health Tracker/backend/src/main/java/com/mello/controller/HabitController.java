package com.mello.controller;

import com.mello.model.Habit;
import com.mello.service.HabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits")
@CrossOrigin(origins = "*") // Allow React frontend to connect
public class HabitController {

    @Autowired
    private HabitService habitService;

    @GetMapping
    public List<Habit> getAllHabits(@RequestParam String userId) {
        return habitService.getAllHabits(userId);
    }

    @PostMapping
    public Habit createHabit(@RequestBody Habit habit) {
        return habitService.createHabit(habit);
    }

    @DeleteMapping("/{id}")
    public void deleteHabit(@PathVariable String id) {
        habitService.deleteHabit(id);
    }

    @PutMapping("/{id}")
    public Habit updateHabit(@PathVariable String id, @RequestBody Habit habit) {
        return habitService.updateHabit(id, habit);
    }
}
