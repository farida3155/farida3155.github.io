package com.mello.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "master_recommendations")
public class MasterRecommendation {
    @Id
    private String id;
    private String title;
    private String description;
    private String emojiCode;
    private String emojiAlt;
    private String moodCategory; // "low", "stressed", "balanced", "any"
    private String type; // "base", "smart", "micro"

    public MasterRecommendation() {}

    public MasterRecommendation(String title, String description, String emojiCode, String emojiAlt, String moodCategory, String type) {
        this.title = title;
        this.description = description;
        this.emojiCode = emojiCode;
        this.emojiAlt = emojiAlt;
        this.moodCategory = moodCategory;
        this.type = type;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getEmojiCode() { return emojiCode; }
    public void setEmojiCode(String emojiCode) { this.emojiCode = emojiCode; }
    public String getEmojiAlt() { return emojiAlt; }
    public void setEmojiAlt(String emojiAlt) { this.emojiAlt = emojiAlt; }
    public String getMoodCategory() { return moodCategory; }
    public void setMoodCategory(String moodCategory) { this.moodCategory = moodCategory; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
