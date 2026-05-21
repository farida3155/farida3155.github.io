package com.mello.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "recommendations")
public class RecommendationEntry {
    @Id
    private String id;
    private String userId;
    private String mood;
    private RecommendationBundle bundle;
    private LocalDateTime timestamp;

    public RecommendationEntry() {}

    public RecommendationEntry(String userId, String mood, RecommendationBundle bundle) {
        this.userId = userId;
        this.mood = mood;
        this.bundle = bundle;
        this.timestamp = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }
    public RecommendationBundle getBundle() { return bundle; }
    public void setBundle(RecommendationBundle bundle) { this.bundle = bundle; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
