package com.mello.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "habits")
 public class Habit {
 
     @Id
     private String id;
     
     private String userId;
     private String name;
     private String description;
     
     // Tracks progress for the current week (M, T, W, T, F, S, S)
     private List<Boolean> completedDays;
     private Integer progress;
 
     // New Fields for Periodicity and Reminders
     private List<String> activeDays; // e.g. ["MON", "WED", "FRI"]
     private Integer durationMinutes; // e.g. 15, 30, 60
     private String reminderTime; // e.g. "08:00 AM"
 
     // Periodicity and Advanced Tracking
     private String periodicity; // "daily", "weekly", "monthly"
     private Integer targetCount; // How many times to complete in the period
     private Integer currentCount; // How many times completed so far
     private Integer streak; // Current consecutive completion streak

     public Habit() {}
 
     // Getters and Setters
     public String getId() { return id; }
     public void setId(String id) { this.id = id; }
     public String getUserId() { return userId; }
     public void setUserId(String userId) { this.userId = userId; }
     public String getName() { return name; }
     public void setName(String name) { this.name = name; }
     public String getDescription() { return description; }
     public void setDescription(String description) { this.description = description; }
     public List<Boolean> getCompletedDays() { return completedDays; }
     public void setCompletedDays(List<Boolean> completedDays) { this.completedDays = completedDays; }
     public Integer getProgress() { return progress; }
     public void setProgress(Integer progress) { this.progress = progress; }
     public List<String> getActiveDays() { return activeDays; }
     public void setActiveDays(List<String> activeDays) { this.activeDays = activeDays; }
     public Integer getDurationMinutes() { return durationMinutes; }
     public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
     public String getReminderTime() { return reminderTime; }
     public void setReminderTime(String reminderTime) { this.reminderTime = reminderTime; }
     public String getPeriodicity() { return periodicity; }
     public void setPeriodicity(String periodicity) { this.periodicity = periodicity; }
     public Integer getTargetCount() { return targetCount; }
     public void setTargetCount(Integer targetCount) { this.targetCount = targetCount; }
     public Integer getCurrentCount() { return currentCount; }
     public void setCurrentCount(Integer currentCount) { this.currentCount = currentCount; }
     public Integer getStreak() { return streak; }
     public void setStreak(Integer streak) { this.streak = streak; }

 }