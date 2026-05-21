package com.mello.service;

import com.mello.model.Mood;
import com.mello.repository.MoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MoodService {
    @Autowired
    private MoodRepository moodRepository;

    public List<Mood> getAllMoods(String userId) {
        if (userId == null) return List.of();
        return moodRepository.findByUserIdOrderByDateDesc(userId);
    }

    public Optional<Mood> getMoodByDate(String userId, String date) {
        if (userId == null) return Optional.empty();
        return moodRepository.findByUserIdAndDate(userId, date);
    }

    public Mood saveMood(Mood mood) {
        if (mood.getUserId() == null) {
            throw new IllegalArgumentException("User identifier is missing");
        }
        
        Optional<Mood> existingMood = moodRepository.findByUserIdAndDate(mood.getUserId(), mood.getDate());
        if (existingMood.isPresent()) {
            Mood existing = existingMood.get();
            existing.setMood(mood.getMood());
            existing.setEmoji(mood.getEmoji());
            return moodRepository.save(existing);
        } else {
            return moodRepository.save(mood);
        }
    }

    public void deleteAllMoods(String userId) {
        if (userId == null || userId.trim().isEmpty()) return;
        // Native-style delete to ensure only this user's moods are gone
        moodRepository.deleteByUserId(userId);
    }

    public boolean deleteMoodByDate(String userId, String date) {
        if (userId == null) return false;
        Optional<Mood> mood = moodRepository.findByUserIdAndDate(userId, date);
        if (mood.isPresent()) {
            moodRepository.delete(mood.get());
            return true;
        }
        return false;
    }

    public MoodStats getMoodStats(String userId) {
        if (userId == null) return new MoodStats();
        List<Mood> allMoods = moodRepository.findByUserId(userId);
        MoodStats stats = new MoodStats();
        if (allMoods.isEmpty()) return stats;

        stats.setTotalEntries(allMoods.size());
        for (Mood mood : allMoods) {
            if (mood.getMood() == null) continue;
            switch (mood.getMood().toLowerCase()) {
                case "happy": stats.setHappyCount(stats.getHappyCount() + 1); break;
                case "calm": stats.setCalmCount(stats.getCalmCount() + 1); break;
                case "sad": stats.setSadCount(stats.getSadCount() + 1); break;
                case "anxious": stats.setAnxiousCount(stats.getAnxiousCount() + 1); break;
                case "tired": stats.setTiredCount(stats.getTiredCount() + 1); break;
                case "angry": stats.setAngryCount(stats.getAngryCount() + 1); break;
            }
        }
        return stats;
    }

    public static class MoodStats {
        private int totalEntries = 0;
        private int happyCount = 0;
        private int calmCount = 0;
        private int sadCount = 0;
        private int anxiousCount = 0;
        private int tiredCount = 0;
        private int angryCount = 0;

        public int getTotalEntries() { return totalEntries; }
        public void setTotalEntries(int totalEntries) { this.totalEntries = totalEntries; }
        public int getHappyCount() { return happyCount; }
        public void setHappyCount(int happyCount) { this.happyCount = happyCount; }
        public int getCalmCount() { return calmCount; }
        public void setCalmCount(int calmCount) { this.calmCount = calmCount; }
        public int getSadCount() { return sadCount; }
        public void setSadCount(int sadCount) { this.sadCount = sadCount; }
        public int getAnxiousCount() { return anxiousCount; }
        public void setAnxiousCount(int anxiousCount) { this.anxiousCount = anxiousCount; }
        public int getTiredCount() { return tiredCount; }
        public void setTiredCount(int tiredCount) { this.tiredCount = tiredCount; }
        public int getAngryCount() { return angryCount; }
        public void setAngryCount(int angryCount) { this.angryCount = angryCount; }
    }
}
