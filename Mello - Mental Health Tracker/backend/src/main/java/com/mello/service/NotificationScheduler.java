package com.mello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificationScheduler {

    @Autowired
    private NotificationService notificationService;

    // Every day shortly after midnight: create today's reminders.
    @Scheduled(cron = "0 5 0 * * *")
    public void scheduleDailyReminders() {
        notificationService.scheduleTodayHabitReminders();
    }

    // Every minute: ensure today's reminders are scheduled and deliver any due ones.
    @Scheduled(cron = "0 * * * * *")
    public void ensureTodayRemindersExist() {
        notificationService.scheduleTodayHabitReminders();
        notificationService.deliverDueScheduledNotifications();
    }

    // Daily morning: AFK check.
    @Scheduled(cron = "0 0 9 * * *")
    public void afkCheck() {
        notificationService.sendAfkNotificationsIfNeeded();
    }

    // Daily late night: missed-habit check for "today".
    @Scheduled(cron = "0 59 23 * * *")
    public void missedHabits() {
        notificationService.sendMissedHabitNotificationsForToday();
    }

    // Daily digest email at 8 AM: one summary email per user instead of per-event spam.
    @Scheduled(cron = "0 0 8 * * *")
    public void dailyDigest() {
        notificationService.sendDailyDigestEmails();
    }
}
