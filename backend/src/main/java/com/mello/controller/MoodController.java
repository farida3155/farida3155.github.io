package com.mello.controller;

import com.mello.model.Mood;
import com.mello.service.MoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/moods")
@CrossOrigin(origins = "*")
public class MoodController {
    @Autowired
    private MoodService moodService;

    @GetMapping
    public ResponseEntity<List<Mood>> getAllMoods(@RequestParam String userId) {
        System.out.println("Fetching moods for userId: " + userId);
        return ResponseEntity.ok(moodService.getAllMoods(userId));
    }

    @GetMapping("/{date}")
    public ResponseEntity<Mood> getMoodByDate(@RequestParam String userId, @PathVariable String date) {
        return moodService.getMoodByDate(userId, date)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Mood> saveMood(@RequestBody Mood mood) {
        if (mood.getDate() == null || mood.getMood() == null || mood.getUserId() == null) 
            return ResponseEntity.badRequest().build();
        return ResponseEntity.status(HttpStatus.CREATED).body(moodService.saveMood(mood));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllMoods(@RequestParam String userId) {
        moodService.deleteAllMoods(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<MoodService.MoodStats> getMoodStats(@RequestParam String userId) {
        return ResponseEntity.ok(moodService.getMoodStats(userId));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "Mood Tracker API"));
    }
}
