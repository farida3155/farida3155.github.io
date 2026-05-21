package com.mello.service;

import com.mello.model.Habit;
import com.mello.model.Notification;
import com.mello.model.User;
import com.mello.repository.HabitRepository;
import com.mello.repository.NotificationRepository;
import com.mello.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class HabitReminderScheduler {

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EmailService emailService;

    // Runs every minute to check for habit reminders
    @Scheduled(cron = "0 * * * * *")
    public void checkAndSendHabitReminders() {
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);
        String todayDateString = LocalDateTime.now().toLocalDate().toString();
        List<Habit> allHabits = habitRepository.findAll();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        // Also support "HH:mm" if they didn't use AM/PM
        DateTimeFormatter formatter24h = DateTimeFormatter.ofPattern("HH:mm");

        for (Habit habit : allHabits) {
            if (habit.getReminderTime() == null || habit.getReminderTime().trim().isEmpty()) {
                continue;
            }

            LocalTime reminderTime = null;
            try {
                if (habit.getReminderTime().toUpperCase().contains("AM") || habit.getReminderTime().toUpperCase().contains("PM")) {
                    reminderTime = LocalTime.parse(habit.getReminderTime().toUpperCase(), formatter);
                } else {
                    reminderTime = LocalTime.parse(habit.getReminderTime(), formatter24h);
                }
            } catch (DateTimeParseException e) {
                // Ignore invalid time formats
                continue;
            }

            // Check exact time match
            if (now.equals(reminderTime)) {
                processReminder(habit, todayDateString, "EXACT", "It's time");
            }

            // Check 10 minutes prior
            if (now.plusMinutes(10).equals(reminderTime)) {
                processReminder(habit, todayDateString, "10MIN", "Just 10 minutes left");
            }
        }
    }

    private void processReminder(Habit habit, String dateString, String typeSuffix, String timeMessage) {
        String eventKey = "HABIT_REMINDER_" + typeSuffix + "_" + habit.getId() + "_" + dateString;

        // Check if notification already sent to avoid duplicates
        Optional<Notification> existingOpt = notificationRepository.findByUserIdAndEventKey(habit.getUserId(), eventKey);
        if (existingOpt.isPresent()) {
            return;
        }

        Optional<User> userOpt = userRepository.findById(habit.getUserId());
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Create in-app notification
            Notification notification = new Notification();
            notification.setUserId(user.getId());
            notification.setType("HABIT_REMINDER");
            notification.setTitle("Time for " + habit.getName());
            notification.setMessage(timeMessage + " to complete your habit!");
            notification.setHabitId(habit.getId());
            notification.setHabitName(habit.getName());
            notification.setEventKey(eventKey);
            notification.setStatus("DELIVERED");
            notification.setCreatedAt(LocalDateTime.now());
            notification.setDeliveredAt(LocalDateTime.now());
            notification.setRead(false);
            
            notificationRepository.save(notification);

            // Send Email
            try {
                emailService.sendHabitReminderEmail(user.getEmail(), user.getFullName(), habit.getName(), timeMessage);
            } catch (Exception e) {
                System.err.println("Failed to send habit reminder email: " + e.getMessage());
            }
        }
    }
}
