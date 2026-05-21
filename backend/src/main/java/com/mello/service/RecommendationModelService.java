package com.mello.service;
 
 import java.util.ArrayList;
 import java.util.List;
 
 import org.springframework.stereotype.Service;
 
 import com.mello.model.HabitSignal;
 import com.mello.model.RecommendationBundle;
 import com.mello.model.SuggestionItem;
 
import com.mello.repository.MasterRecommendationRepository;
import com.mello.model.MasterRecommendation;
import jakarta.annotation.PostConstruct;
import java.util.stream.Collectors;

@Service
public class RecommendationModelService {
    private final MasterRecommendationRepository masterRecommendationRepository;

    public RecommendationModelService(MasterRecommendationRepository masterRecommendationRepository) {
        this.masterRecommendationRepository = masterRecommendationRepository;
    }

    @PostConstruct
    public void initData() {
        // Clear existing to ensure all our new additions are loaded
        masterRecommendationRepository.deleteAll();
        
        List<MasterRecommendation> initial = List.of(
            // --- Base Suggestions ---
            new MasterRecommendation("Take a walk", "A 10-minute walk can ease stress and clear your mind.", "1F6B6", "Walking person", "low", "base"),
            new MasterRecommendation("Drink water", "Hydration helps your energy and focus during emotional dips.", "1F4A7", "Water droplet", "low", "base"),
            new MasterRecommendation("Breathing exercise", "Try a 4-4-4 breathing cycle for 3 minutes.", "1FAC1", "Lungs", "low", "base"),
            
            new MasterRecommendation("Stretch break", "Release tension with a short full-body stretch routine.", "1F9D8", "Person in lotus position", "stressed", "base"),
            new MasterRecommendation("Write your thoughts", "Journal for 5 minutes to unload mental pressure.", "270D-FE0F", "Writing hand", "stressed", "base"),
            new MasterRecommendation("Digital pause", "Take a 15-minute break away from screens.", "1F4F5", "No mobile phones", "stressed", "base"),
            
            new MasterRecommendation("Take a walk", "Keep your momentum with light movement.", "1F6B6", "Walking person", "balanced", "base"),
            new MasterRecommendation("Drink water", "Small healthy actions keep your routine stable.", "1F4A7", "Water droplet", "balanced", "base"),
            new MasterRecommendation("Gratitude note", "Write one thing that went well today.", "2728", "Sparkles", "balanced", "base"),

            // --- Smart Suggestions ---
            new MasterRecommendation("Morning reset walk", "You've had low-energy mornings. Try a short walk after waking up.", "1F305", "Sunrise", "any", "smart"),
            new MasterRecommendation("Reflection checkpoint", "Keep your momentum with one short reflection tonight.", "1F4D3", "Notebook", "any", "smart"),
            new MasterRecommendation("Pressure release", "Short movement breaks help reduce stress load during the day.", "1F9D8", "Person in lotus position", "stressed", "smart"),
            new MasterRecommendation("Listen to nature sounds", "A quick auditory escape with nature sounds supports emotional balance.", "1F3A7", "Headphones", "any", "smart"),

            // --- Micro Actions ---
            new MasterRecommendation("Take 3 deep breaths", "1 min", "1FAC1", "Lungs", "any", "micro"),
            new MasterRecommendation("Text someone you trust", "2 min", "1F4AC", "Speech balloon", "any", "micro"),
            new MasterRecommendation("Step outside for fresh air", "5 min", "1F324", "Sun behind small cloud", "any", "micro"),
            new MasterRecommendation("Relax your shoulders + jaw", "1 min", "1F9D8", "Person in lotus position", "stressed", "micro")
        );
        masterRecommendationRepository.saveAll(initial);
    }

    public RecommendationBundle generateRecommendations(String mood, List<HabitSignal> habits, int weeklyCheckins) {
        double avgCompletion = habits.stream().mapToDouble(HabitSignal::getCompletionRate).average().orElse(0.5);
        boolean journalDropped = habits.stream().anyMatch(h ->
            h.getHabitName().toLowerCase().contains("journal") && h.getLastCompletedDaysAgo() >= 3
        );
        boolean workoutDropped = habits.stream().anyMatch(h ->
            (h.getHabitName().toLowerCase().contains("workout") || h.getHabitName().toLowerCase().contains("exercise"))
                && h.getCompletionRate() < 0.45
        );

        List<SuggestionItem> base = fetchFromDb(mood, "base");
        List<SuggestionItem> smart = fetchFromDb(mood, "smart");
        // Fallback to "any" if none found for specific mood
        if (smart.isEmpty()) smart = fetchFromDb("any", "smart");
        
        List<SuggestionItem> micro = fetchFromDb(mood, "micro");
        if (micro.isEmpty()) micro = fetchFromDb("any", "micro");

        List<String> plan = weeklyPlan(habits, mood);
        String risk = gentleRiskAlert(mood, avgCompletion);
        List<String> reinforcement = positiveReinforcement(mood, weeklyCheckins, avgCompletion);

        return new RecommendationBundle(base, smart, micro, plan, risk, reinforcement);
    }

    private List<SuggestionItem> fetchFromDb(String mood, String type) {
        return masterRecommendationRepository.findByMoodCategoryAndType(mood.toLowerCase(), type)
                .stream()
                .map(m -> card(m.getTitle(), m.getDescription(), m.getEmojiCode(), m.getEmojiAlt()))
                .collect(Collectors.toList());
    }

 
 
     private List<String> weeklyPlan(List<HabitSignal> habits, String mood) {
         List<String> items = new ArrayList<>();
         items.add("Keep sleep timing within a 45-minute window");
         if (habits.stream().anyMatch(h -> h.getCompletionRate() < 0.5)) {
             items.add("Re-engage one low-consistency habit for 3 days");
         }
         items.add("2 sessions of listening to nature sounds");
         if ("stressed".equalsIgnoreCase(mood)) {
             items.add("Daily 5-minute decompression break");
         } else {
             items.add("3 short movement sessions this week");
         }
         return items.stream().limit(4).toList();
     }
 
     private String gentleRiskAlert(String mood, double avgCompletion) {
         if (avgCompletion < 0.45 || "low".equalsIgnoreCase(mood) || "stressed".equalsIgnoreCase(mood)) {
             return "We noticed some patterns that might need extra care this week. Keep actions very small and steady.";
         }
         return "We noticed a few patterns that might need extra care this week. You're stable overall - keep protecting your routine.";
     }
 
     private List<String> positiveReinforcement(String mood, int weeklyCheckins, double avgCompletion) {
         if ("stressed".equalsIgnoreCase(mood)) {
             return List.of(
                 "You still kept healthy actions during stressful days - that's progress.",
                 "Your consistency is protecting you more than you think.",
                 "Each short reset you take helps your future self."
             );
         }
         if (weeklyCheckins >= 4 || avgCompletion >= 0.6) {
             return List.of(
                 "You showed up " + weeklyCheckins + " days this week - that matters.",
                 "Even small steps count, and you're taking them.",
                 "This consistency is creating long-term emotional stability."
             );
         }
         return List.of(
             "You're still showing up, even on hard days.",
             "Small actions are enough to restart momentum.",
             "Your future self benefits from each tiny step."
         );
     }
 
     private SuggestionItem card(String title, String description, String openMojiCode, String emojiAlt) {
         return new SuggestionItem(title, description, openMojiUrl(openMojiCode), emojiAlt);
     }
 
     private String openMojiUrl(String code) {
         return "https://cdn.jsdelivr.net/gh/hfg-gmuend/openmoji/color/svg/" + code + ".svg";
     }
 }
