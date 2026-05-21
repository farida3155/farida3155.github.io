package com.mello.repository;

import com.mello.model.HabitLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

public interface HabitLogRepository extends MongoRepository<HabitLog, String> {
    List<HabitLog> findByUserIdAndTimestampBetween(String userId, LocalDateTime start, LocalDateTime end);
    List<HabitLog> findByUserId(String userId);

// NOURHAN
    Optional<HabitLog> findTopByUserIdOrderByTimestampDesc(String userId);

    boolean existsByHabitIdAndUserIdAndStatusIgnoreCaseAndTimestampBetween(
            String habitId,
            String userId,
            String status,
            LocalDateTime start,
            LocalDateTime end
    );
}
