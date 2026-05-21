package com.mello.service;

import com.mello.model.Habit;
import com.mello.model.HabitLog;
import com.mello.model.Notification;
import com.mello.model.User;
import com.mello.repository.HabitLogRepository;
import com.mello.repository.HabitRepository;
import com.mello.repository.NotificationRepository;
import com.mello.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

// NOURHAN
@Service
public class NotificationService {

    private static final List<DateTimeFormatter> REMINDER_TIME_FORMATS = List.of(
            DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("H:mm", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH));
    private static final DateTimeFormatter QUOTE_SLOT_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    private static final List<String> WELLNESS_QUOTES = List.of(
            "Small steps every day build lasting change.",
            "Your progress does not need to be perfect to be meaningful.",
            "Rest is productive when your mind and body need it.",
            "A calm breath can reset a difficult moment.",
            "Consistency matters more than intensity.",
            "Healing and growth can happen at the same time.",
            "Be as kind to yourself as you are to others.",
            "You are allowed to pause without giving up.",
            "One healthy choice now can shape your whole day.",
            "Your future self will thank you for today's effort.",
            "It is okay to start again as many times as needed.",
            "Protect your energy; it is one of your greatest assets.",
            "Your pace is valid, even when it is slower than expected.",
            "A clear mind starts with one intentional habit.",
            "Hard days do not erase your progress.",
            "You can do difficult things, one moment at a time.",
            "Discipline grows when you keep promises to yourself.",
            "You deserve routines that support your peace.",
            "Focus on what you can control right now.",
            "Every mindful choice is a vote for your wellbeing.");

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private HabitRepository habitRepository;
    @Autowired
    private HabitLogRepository habitLogRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void ensureNotificationIndexes() {
        try {
            mongoTemplate.indexOps(Notification.class).ensureIndex(
                    new Index()
                            .on("userId", Sort.Direction.ASC)
                            .on("eventKey", Sort.Direction.ASC)
                            .unique()
                            .sparse()
                            .named("uniq_notification_user_event"));
        } catch (Exception e) {
            System.err.println("Failed to ensure notification unique index: " + e.getMessage());
        }
    }

    public List<Notification> getNotifications(String userId) {
        // Only expose delivered notifications to clients. SCHEDULED entries are
        // internal queue records.
        return notificationRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, "DELIVERED");
    }

    public Optional<Notification> markRead(String notificationId) {
        return notificationRepository.findById(notificationId).map(n -> {
            n.setRead(true);
            n.setReadAt(LocalDateTime.now());
            return notificationRepository.save(n);
        });
    }

    public void onHabitAdded(Habit habit) {
        String today = LocalDate.now().toString();
        String eventKey = "HABIT_ADDED:" + habit.getId() + ":" + today;
        if (notificationRepository.findByUserIdAndEventKey(habit.getUserId(), eventKey).isPresent())
            return;

        Notification n = base(habit.getUserId(), "HABIT_ADDED", eventKey);
        n.setTitle("New habit added");
        n.setMessage("You added \"" + safe(habit.getName()) + "\".");
        n.setHabitId(habit.getId());
        n.setHabitName(habit.getName());
        deliverNow(n);

        scheduleTodayHabitReminderFor(habit);
    }

    public void scheduleTodayHabitReminderFor(Habit habit) {
        if (habit == null)
            return;
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        if (habit.getUserId() == null)
            return;
        if (habit.getReminderTime() == null || habit.getReminderTime().isBlank())
            return;

        LocalTime reminder = parseReminderTime(habit.getReminderTime());
        if (reminder == null)
            return;
        if (!isHabitActiveOnDate(habit, today))
            return;

        LocalDateTime reminderAt = today.atTime(reminder);
        long minutesUntilReminder = Duration.between(now, reminderAt).toMinutes();

        // Send only when the reminder time is within the next 15 minutes (inclusive).
        if (minutesUntilReminder < 0 || minutesUntilReminder > 15)
            return;

        String eventKey = "HABIT_REMINDER:" + habit.getId() + ":" + today;
        if (notificationRepository.findByUserIdAndEventKey(habit.getUserId(), eventKey).isPresent())
            return;

        long minutesLeft = Math.max(0, minutesUntilReminder);
        Notification n = base(habit.getUserId(), "HABIT_REMINDER", eventKey);
        n.setTitle("Habit reminder");
        n.setMessage("Reminder: \"" + safe(habit.getName()) + "\" starts in " + minutesLeft + " minute"
                + (minutesLeft == 1 ? "" : "s") + ".");
        n.setHabitId(habit.getId());
        n.setHabitName(habit.getName());
        deliverNow(n);
    }

    public void onHabitDone(HabitLog log) {
        if (log == null)
            return;
        if (log.getUserId() == null)
            return;
        if (!"COMPLETED".equalsIgnoreCase(safe(log.getStatus())))
            return;

        String date = (log.getTimestamp() != null ? log.getTimestamp().toLocalDate() : LocalDate.now()).toString();
        String eventKey = "HABIT_DONE:" + safe(log.getHabitId()) + ":" + date;
        if (notificationRepository.findByUserIdAndEventKey(log.getUserId(), eventKey).isPresent())
            return;

        Notification n = base(log.getUserId(), "HABIT_DONE", eventKey);
        n.setTitle("Habit completed");
        n.setMessage("Nice work! You completed \"" + safe(log.getHabitName()) + "\".");
        n.setHabitId(log.getHabitId());
        n.setHabitName(log.getHabitName());
        deliverNow(n);
    }

    public void scheduleTodayHabitReminders() {
        for (Habit habit : habitRepository.findAll()) {
            scheduleTodayHabitReminderFor(habit);
        }
    }

    public void scheduleUpcomingHabitReminders(int daysAhead, LocalDateTime now) {
        LocalDate startDate = now.toLocalDate();
        for (Habit habit : habitRepository.findAll()) {
            if (habit.getUserId() == null)
                continue;
            if (habit.getReminderTime() == null || habit.getReminderTime().isBlank())
                continue;
            LocalTime reminder = parseReminderTime(habit.getReminderTime());
            if (reminder == null)
                continue;

            scheduleUpcomingHabitRemindersForHabit(habit, daysAhead, startDate, reminder);
        }
    }

    private void scheduleUpcomingHabitRemindersForHabit(Habit habit, int daysAhead, LocalDate startDate,
            LocalTime reminder) {
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i <= daysAhead; i++) {
            LocalDate date = startDate.plusDays(i);
            if (!isHabitActiveOnDate(habit, date))
                continue;

            LocalDateTime scheduledFor = date.atTime(reminder).minusMinutes(15);
            if (scheduledFor.isBefore(now.minusMinutes(1)))
                continue;

            String eventKey = "HABIT_REMINDER:" + habit.getId() + ":" + date;
            if (notificationRepository.findByUserIdAndEventKey(habit.getUserId(), eventKey).isPresent())
                continue;

            Notification n = base(habit.getUserId(), "HABIT_REMINDER", eventKey);
            n.setTitle("Habit reminder");
            n.setMessage("Reminder: \"" + safe(habit.getName()) + "\" starts in 15 minutes.");
            n.setHabitId(habit.getId());
            n.setHabitName(habit.getName());
            n.setScheduledFor(scheduledFor);
            n.setStatus("SCHEDULED");
            n.setRead(false);
            n.setCreatedAt(LocalDateTime.now());
            saveIgnoringDuplicateEvent(n);
        }
    }

    public void deliverDueScheduledNotifications() {
        LocalDateTime now = LocalDateTime.now();
        List<Notification> due = notificationRepository.findByStatusAndScheduledForLessThanEqual("SCHEDULED", now);
        for (Notification n : due) {
            // Habit reminders are delivered by the 0..15 minute window check.
            // Remove legacy queued reminder rows to prevent duplicate sends.
            if ("HABIT_REMINDER".equalsIgnoreCase(safe(n.getType()))) {
                notificationRepository.delete(n);
                continue;
            }
            n.setStatus("DELIVERED");
            n.setDeliveredAt(now);
            if (n.getCreatedAt() == null)
                n.setCreatedAt(now);
            saveIgnoringDuplicateEvent(n);
            // No per-event email — the daily digest handles all email delivery.
        }
    }

    public void sendAfkNotificationsIfNeeded() {
        LocalDate today = LocalDate.now();
        LocalDateTime cutoff = LocalDateTime.now().minusDays(7);

        for (User user : userRepository.findAll()) {
            if (user.getId() == null)
                continue;

            LocalDateTime lastActivity = habitLogRepository.findTopByUserIdOrderByTimestampDesc(user.getId())
                    .map(HabitLog::getTimestamp)
                    .orElse(null);

            if (lastActivity == null || lastActivity.isAfter(cutoff))
                continue;

            String eventKey = "AFK_7_DAYS:" + user.getId() + ":" + today;
            if (notificationRepository.findByUserIdAndEventKey(user.getId(), eventKey).isPresent())
                continue;

            Notification n = base(user.getId(), "AFK_7_DAYS", eventKey);
            n.setTitle("We miss you");
            n.setMessage("You’ve been away for 7+ days. Want to get back on track today?");
            n.setMeta(Map.of("lastActivity", lastActivity.toString()));
            deliverNow(n);

            if (user.getEmail() != null && !user.getEmail().isBlank()) {
                String subject = "Mello Check-in: We miss you! 🌿";
                String body = "Hello " + user.getFullName() + ",\n\n" +
                        "You’ve been away for 7+ days. Want to get back on track today?\n\n" +
                        "Take a moment for yourself and log a reflection or complete a habit.\n\n" +
                        "Stay mindful,\nThe Mello Team";
                try {
                    emailService.sendNotificationEmail(user.getEmail(), subject, body);
                } catch (Exception e) {
                    System.err.println("Failed to send AFK email: " + e.getMessage());
                }
            }
        }
    }

    public void sendMissedHabitNotificationsForToday() {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(23, 59, 59);

        for (Habit habit : habitRepository.findAll()) {
            if (habit.getUserId() == null)
                continue;
            if (!isHabitActiveOnDate(habit, today))
                continue;

            boolean completed = habitLogRepository
                    .existsByHabitIdAndUserIdAndStatusIgnoreCaseAndTimestampBetween(
                            habit.getId(), habit.getUserId(), "COMPLETED", start, end);
            if (completed)
                continue;

            String eventKey = "HABIT_MISSED:" + habit.getId() + ":" + today;
            if (notificationRepository.findByUserIdAndEventKey(habit.getUserId(), eventKey).isPresent())
                continue;

            Notification n = base(habit.getUserId(), "HABIT_MISSED", eventKey);
            n.setTitle("Habit missed");
            n.setMessage("You missed \"" + safe(habit.getName()) + "\" today. Tomorrow is a fresh start.");
            n.setHabitId(habit.getId());
            n.setHabitName(habit.getName());
            deliverNow(n);
        }
    }

    public void sendWellnessQuotesEveryThirtyMinutes() {
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        LocalDateTime slotStart = now.withMinute((now.getMinute() / 30) * 30).withSecond(0).withNano(0);
        long slotBucket = slotStart.atZone(ZoneId.systemDefault()).toEpochSecond() / (30 * 60);
        String slotKey = slotStart.format(QUOTE_SLOT_FORMATTER);

        for (User user : userRepository.findAll()) {
            if (user.getId() == null || user.getId().isBlank())
                continue;

            String eventKey = "WELLNESS_QUOTE:" + user.getId() + ":" + slotKey;
            if (notificationRepository.findByUserIdAndEventKey(user.getId(), eventKey).isPresent())
                continue;

            int quoteIndex = Math.floorMod(Objects.hash(user.getId(), slotBucket), WELLNESS_QUOTES.size());
            String quote = WELLNESS_QUOTES.get(quoteIndex);

            Notification n = base(user.getId(), "WELLNESS_QUOTE", eventKey);
            n.setTitle("Wellness quote");
            n.setMessage(quote);
            n.setMeta(Map.of(
                    "quoteIndex", quoteIndex,
                    "slotStart", slotStart.toString()));
            deliverNow(n);
        }
    }

    private static boolean isHabitActiveOnDate(Habit habit, LocalDate date) {
        if (habit == null)
            return false;

        String p = habit.getPeriodicity();
        String periodicity = p == null ? "" : p.trim().toLowerCase(Locale.ENGLISH);

        List<String> active = habit.getActiveDays();
        if (active == null)
            active = List.of();

        if ("weekly".equals(periodicity)) {
            // Weekly: require matching day-of-week if activeDays provided; otherwise treat
            // as every day.
            if (active.isEmpty())
                return true;
            DayOfWeek dow = date.getDayOfWeek();
            for (String d : active) {
                if (d == null)
                    continue;
                try {
                    if (DayOfWeek.valueOf(d.trim().toUpperCase(Locale.ENGLISH)) == dow)
                        return true;
                } catch (IllegalArgumentException ignored) {
                }
            }
            return false;
        }

        if ("monthly".equals(periodicity)) {
            // Monthly: if activeDays contains numeric day-of-month strings ("1".."31"),
            // match them.
            // If not provided, fall back to every day (so reminders still work without
            // extra fields).
            if (active.isEmpty())
                return true;
            int dom = date.getDayOfMonth();
            for (String d : active) {
                if (d == null)
                    continue;
                String t = d.trim();
                try {
                    if (Integer.parseInt(t) == dom)
                        return true;
                } catch (NumberFormatException ignored) {
                }
            }
            return false;
        }

        // Daily or unspecified: active every day.
        return true;
    }

    private static LocalTime parseReminderTime(String reminderTime) {
        if (reminderTime == null)
            return null;
        String raw = reminderTime.trim();
        if (raw.isBlank())
            return null;

        String normalized = raw.replaceAll("\\s+", " ").trim();
        // Defensive cleanup for values coming from DB/UIs like "18:46," or "06:31."
        normalized = normalized.replaceAll("[,.;]$", "");
        // Drop seconds if present (e.g. "18:46:00" -> "18:46")
        normalized = normalized.replaceAll("^(\\d{1,2}:\\d{2}):\\d{2}(\\s*[AP]M)?$", "$1$2");
        // If user typed "6:31am" -> "6:31 am"
        normalized = normalized.replaceAll("(?i)(\\d)\\s*(am|pm)\\b", "$1 $2");
        // Uppercase AM/PM for the formatters using Locale.ENGLISH
        normalized = normalized.replaceAll("(?i)\\bam\\b", "AM");
        normalized = normalized.replaceAll("(?i)\\bpm\\b", "PM");

        for (DateTimeFormatter fmt : REMINDER_TIME_FORMATS) {
            try {
                return LocalTime.parse(normalized, fmt);
            } catch (DateTimeParseException ignored) {
            }
        }
        return null;
    }

    private Notification base(String userId, String type, String eventKey) {
        Notification n = new Notification();
        n.setUserId(userId);
        n.setType(type);
        n.setEventKey(eventKey);
        n.setRead(false);
        n.setCreatedAt(LocalDateTime.now());
        return n;
    }

    private void deliverNow(Notification n) {
        LocalDateTime now = LocalDateTime.now();
        n.setStatus("DELIVERED");
        n.setDeliveredAt(now);
        if (n.getCreatedAt() == null)
            n.setCreatedAt(now);
        saveIgnoringDuplicateEvent(n);
        // No per-event email — the daily digest handles all email delivery.
    }

    /**
     * Sends one daily digest email per user summarising their unread notifications.
     * Called once a day by the scheduler — this is the ONLY place emails are sent.
     */
    public void sendDailyDigestEmails() {
        String today = LocalDate.now().toString();

        for (User user : userRepository.findAll()) {
            if (user.getId() == null || user.getId().isBlank())
                continue;
            String to = user.getEmail();
            if (to == null || to.isBlank())
                continue;

            // Count unread delivered notifications for this user.
            long unread = notificationRepository
                    .findByUserIdAndStatusOrderByCreatedAtDesc(user.getId(), "DELIVERED")
                    .stream()
                    .filter(n -> !Boolean.TRUE.equals(n.isRead()))
                    .count();

            if (unread == 0)
                continue; // Nothing to report — skip this user today.

            // Guard: send at most one digest per user per day.
            String eventKey = "DAILY_DIGEST:" + user.getId() + ":" + today;
            if (notificationRepository.findByUserIdAndEventKey(user.getId(), eventKey).isPresent())
                continue;

            // Save a marker so we don't double-send.
            Notification marker = base(user.getId(), "DAILY_DIGEST", eventKey);
            marker.setTitle("Daily digest");
            marker.setMessage("You have " + unread + " notification" + (unread == 1 ? "" : "s") + " on Mello.");
            deliverNow(marker);

            // As per user requirement, only reminders and check-ins are sent as emails.
            // Daily digest emails are disabled.
        }
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }

    private Notification saveIgnoringDuplicateEvent(Notification n) {
        try {
            return notificationRepository.save(n);
        } catch (DuplicateKeyException ignored) {
            // Another scheduler invocation or node already inserted this event key.
            return null;
        }
    }
}
