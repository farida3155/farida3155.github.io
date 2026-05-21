package com.mello.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "moods")
 public class Mood {
     @Id
     private String id;
     
     @Field("userId")
     private String userId; // Associated User Email
     
     private String date; // Format: YYYY-MM-DD
     private String mood; // happy, calm, sad, anxious, tired, angry
     private String emoji; // 😊, 😌, 😔, 😰, 😴, 😡
 
     public Mood() {}
 
     public Mood(String id, String userId, String date, String mood, String emoji) {
         this.id = id;
         this.userId = userId;
         this.date = date;
         this.mood = mood;
         this.emoji = emoji;
     }
 
     public String getId() { return id; }
     public void setId(String id) { this.id = id; }
     
     public String getUserId() { return userId; }
     public void setUserId(String userId) { this.userId = userId; }
     
     public String getDate() { return date; }
     public void setDate(String date) { this.date = date; }
     
     public String getMood() { return mood; }
     public void setMood(String mood) { this.mood = mood; }
     
     public String getEmoji() { return emoji; }
     public void setEmoji(String emoji) { this.emoji = emoji; }
 }
