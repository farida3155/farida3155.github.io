package com.mello.controller;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.http.ResponseEntity;

import com.mello.model.HabitSignal;
import com.mello.model.RecommendationBundle;
import com.mello.model.Mood;
import com.mello.model.HabitLog;
import com.mello.model.User;
import com.mello.model.RecommendationEntry;
import com.mello.repository.MoodRepository;
import com.mello.repository.HabitLogRepository;
import com.mello.repository.UserRepository;
import com.mello.repository.RecommendationRepository;
import com.mello.service.RecommendationModelService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class DashboardController {
    private final RecommendationModelService recommendationModelService;
    private final MoodRepository moodRepository;
    private final HabitLogRepository habitLogRepository;
    private final UserRepository userRepository;
    private final RecommendationRepository recommendationRepository;

    public DashboardController(
            RecommendationModelService recommendationModelService,
            MoodRepository moodRepository,
            HabitLogRepository habitLogRepository,
            UserRepository userRepository,
            RecommendationRepository recommendationRepository) {
        this.recommendationModelService = recommendationModelService;
        this.moodRepository = moodRepository;
        this.habitLogRepository = habitLogRepository;
        this.userRepository = userRepository;
        this.recommendationRepository = recommendationRepository;
    }


    @GetMapping("/analytics")
    public ResponseEntity<?> analytics(@RequestParam String userId) {
        // Resolve userId to the database ID if an email was provided
        String dbUserId = userId;
        if (userId.contains("@")) {
            dbUserId = userRepository.findByEmail(userId)
                .map(User::getId)
                .orElse(userId);
        }

        // 1. Prepare Last 7 Days Labels

        List<String> labels = new ArrayList<>();
        List<String> dateStrings = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter labelFormatter = DateTimeFormatter.ofPattern("E");

        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            labels.add(date.format(labelFormatter));
            dateStrings.add(date.format(formatter));
        }

        // 2. Fetch Real Mood Data - Check both email and database ID to ensure no data is missed
        List<Mood> userMoods = new ArrayList<>();
        userMoods.addAll(moodRepository.findByUserId(userId));
        if (!dbUserId.equals(userId)) {
            userMoods.addAll(moodRepository.findByUserId(dbUserId));
        }

        Map<String, String> moodMap = userMoods.stream()
            .collect(Collectors.toMap(Mood::getDate, Mood::getMood, (a, b) -> a));

        List<Double> moodScores = new ArrayList<>();
        for (String dateStr : dateStrings) {
            moodScores.add(mapMoodToScore(moodMap.get(dateStr)));
        }

        // 3. Fetch Habit Stats - Check both email and database ID
        List<HabitLog> logs = new ArrayList<>();
        logs.addAll(habitLogRepository.findByUserId(userId));
        if (!dbUserId.equals(userId)) {
            logs.addAll(habitLogRepository.findByUserId(dbUserId));
        }


        List<Integer> habitCompletions = new ArrayList<>();
        Set<String> activeDates = new HashSet<>();
        
        int completedCount = 0;
        for (String dateStr : dateStrings) {
            int dayCount = 0;
            for (HabitLog log : logs) {
                if (log.getTimestamp() != null && 
                    log.getTimestamp().toLocalDate().toString().equals(dateStr)) {
                    activeDates.add(dateStr);
                    if ("COMPLETED".equalsIgnoreCase(log.getStatus())) {
                        dayCount++;
                        completedCount++;
                    }
                }
            }
            habitCompletions.add(dayCount);
        }


        // Add mood tracking dates to active dates
        userMoods.stream()
            .map(Mood::getDate)
            .filter(dateStrings::contains)
            .forEach(activeDates::add);

        // Calculate Focus Level (Activeness)
        // Focus Level = (Days Active in last 7 days / 7) * 10
        double focusLevel = (activeDates.size() / 7.0) * 10.0;
        
        // Refined Completion Rate = (Completed Habits / Total Logs) * 100
        int completionRate = logs.isEmpty() ? 0 : (completedCount * 100 / logs.size());

        double weeklyAverageMood = moodScores.stream().mapToDouble(d -> d).filter(d -> d > 0).average().orElse(3.0);
        boolean positiveWeeklyOutcome = weeklyAverageMood >= 3.5;

        return ResponseEntity.ok(Map.of(
            "weeklyLabels", labels,
            "weeklyMoodScores", moodScores,
            "workoutDone", habitCompletions,
            "weeklyInsight", generateInsight(moodScores, focusLevel),
            "weeklyOutcomeType", positiveWeeklyOutcome ? "positive" : "negative",
            "habitCompletionRate", completionRate,
            "streakCount", calculateStreak(logs, userMoods),
            "focusLevel", Math.round(focusLevel * 10.0) / 10.0
        ));
    }

    @GetMapping("/recommendations")
    public ResponseEntity<RecommendationBundle> recommendations(
            @RequestParam(defaultValue = "neutral") String mood, 
            @RequestParam String userId) {
        return ResponseEntity.ok(generateBundle(mood, userId));
    }

    @PostMapping("/recommendations/complete")
    public ResponseEntity<?> markDone(
            @RequestParam String activity, 
            @RequestParam String mood, 
            @RequestParam String userId) {
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "\"" + activity + "\" marked as done. Great progress!",
            "bundle", generateBundle(mood, userId)
        ));
    }

    private RecommendationBundle generateBundle(String mood, String userId) {
        List<HabitLog> logs = habitLogRepository.findByUserId(userId);
        Map<String, List<HabitLog>> groupedLogs = logs.stream()
            .collect(Collectors.groupingBy(log -> log.getHabitName() != null ? log.getHabitName() : "Unknown"));

        List<HabitSignal> habitSignals = new ArrayList<>();
        for (Map.Entry<String, List<HabitLog>> entry : groupedLogs.entrySet()) {
             String name = entry.getKey();
             List<HabitLog> hLogs = entry.getValue();
             
             long completed = hLogs.stream().filter(l -> "COMPLETED".equalsIgnoreCase(l.getStatus())).count();
             double rate = (double) completed / hLogs.size();
 
             // Find last completed date
             LocalDate lastCompleted = hLogs.stream()
                 .filter(l -> "COMPLETED".equalsIgnoreCase(l.getStatus()))
                 .map(l -> l.getTimestamp().toLocalDate())
                 .max(LocalDate::compareTo)
                 .orElse(LocalDate.MIN);
             
             int daysAgo = lastCompleted.equals(LocalDate.MIN) ? 99 : (int) java.time.temporal.ChronoUnit.DAYS.between(lastCompleted, LocalDate.now());
             
             habitSignals.add(new HabitSignal(name, rate, 0, daysAgo));
         }

        if (habitSignals.isEmpty()) {
            habitSignals = List.of(
                new HabitSignal("Sleep routine", 0.5, 0, 0),
                new HabitSignal("Hydration", 0.7, 0, 0)
            );
        }

        RecommendationBundle bundle = recommendationModelService.generateRecommendations(mood, habitSignals, 4);
        
        // Persist to MongoDB
        RecommendationEntry entry = new RecommendationEntry(userId, mood, bundle);
        recommendationRepository.save(entry);

        return bundle;
    }


    private double mapMoodToScore(String mood) {
        if (mood == null) return 0.0;
        switch (mood.toLowerCase()) {
            case "happy": return 5.0;
            case "calm": return 4.0;
            case "tired": return 2.5;
            case "sad": return 2.0;
            case "anxious": return 1.5;
            case "angry": return 1.0;
            default: return 3.0;
        }
    }

    private String generateInsight(List<Double> scores, double focusLevel) {
        if (focusLevel < 3.0) return "We haven't seen you much this week! Consistency helps build focus.";
        double avg = scores.stream().mapToDouble(d -> d).filter(d -> d > 0).average().orElse(0);
        if (avg >= 4.0) return "Excellent focus and high mood! You are in a great flow.";
        return "You are tracking regularly. Small steps lead to big changes.";
    }

    private int calculateStreak(List<HabitLog> logs, List<Mood> moods) {
        Set<LocalDate> activeDays = new HashSet<>();
        logs.stream()
            .filter(l -> "COMPLETED".equalsIgnoreCase(l.getStatus()))
            .map(l -> l.getTimestamp().toLocalDate())
            .forEach(activeDays::add);
        
        moods.stream()
            .map(m -> LocalDate.parse(m.getDate()))
            .forEach(activeDays::add);

        int streak = 0;
        LocalDate today = LocalDate.now();
        while (activeDays.contains(today.minusDays(streak))) {
            streak++;
        }
        return streak;
    }
}
