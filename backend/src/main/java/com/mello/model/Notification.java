package com.mello.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;

import java.time.LocalDateTime;
import java.util.Map;
// NOURHAN
@Document(collection = "notifications")
 @CompoundIndexes({
         @CompoundIndex(name = "uniq_notification_user_event", def = "{'userId': 1, 'eventKey': 1}", unique = true, sparse = true)
 })
 public class Notification {
     @Id
     private String id;
 
     private String userId;
 
     private String type; // "HABIT_REMINDER", "AFK_3_DAYS", "HABIT_DONE", "HABIT_ADDED", "HABIT_MISSED", "WELLNESS_QUOTE"
 
     private String title;
     private String message;
 
     private String habitId;
     private String habitName;
 
     // Used to prevent duplicates (e.g. "HABIT_MISSED:<habitId>:2026-05-04")
     private String eventKey;
 
     private LocalDateTime scheduledFor;
     private LocalDateTime deliveredAt;
     private String status; // "SCHEDULED" | "DELIVERED"
 
     private boolean read;
     private LocalDateTime readAt;
 
     private LocalDateTime createdAt;
 
     private Map<String, Object> meta;
 
     public Notification() {}
 
     // Getters and Setters
     public String getId() { return id; }
     public void setId(String id) { this.id = id; }
     public String getUserId() { return userId; }
     public void setUserId(String userId) { this.userId = userId; }
     public String getType() { return type; }
     public void setType(String type) { this.type = type; }
     public String getTitle() { return title; }
     public void setTitle(String title) { this.title = title; }
     public String getMessage() { return message; }
     public void setMessage(String message) { this.message = message; }
     public String getHabitId() { return habitId; }
     public void setHabitId(String habitId) { this.habitId = habitId; }
     public String getHabitName() { return habitName; }
     public void setHabitName(String habitName) { this.habitName = habitName; }
     public String getEventKey() { return eventKey; }
     public void setEventKey(String eventKey) { this.eventKey = eventKey; }
     public LocalDateTime getScheduledFor() { return scheduledFor; }
     public void setScheduledFor(LocalDateTime scheduledFor) { this.scheduledFor = scheduledFor; }
     public LocalDateTime getDeliveredAt() { return deliveredAt; }
     public void setDeliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; }
     public String getStatus() { return status; }
     public void setStatus(String status) { this.status = status; }
     public boolean isRead() { return read; }
     public void setRead(boolean read) { this.read = read; }
     public LocalDateTime getReadAt() { return readAt; }
     public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }
     public LocalDateTime getCreatedAt() { return createdAt; }
     public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
     public Map<String, Object> getMeta() { return meta; }
     public void setMeta(Map<String, Object> meta) { this.meta = meta; }
 }
