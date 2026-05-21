package com.mello.service;

import com.mello.model.HabitLog;
import com.mello.repository.HabitLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HabitLogService {
    @Autowired
    private HabitLogRepository habitLogRepository;

    @Autowired
    private NotificationService notificationService;

    public HabitLog saveLog(HabitLog log) {
        if (log.getTimestamp() == null) {
            log.setTimestamp(LocalDateTime.now());
        }
        HabitLog saved = habitLogRepository.save(log);
        notificationService.onHabitDone(saved);
        return saved;
    }

    public List<HabitLog> getLogsForToday(String userId) {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        return habitLogRepository.findByUserIdAndTimestampBetween(userId, startOfDay, endOfDay);
    }
}
