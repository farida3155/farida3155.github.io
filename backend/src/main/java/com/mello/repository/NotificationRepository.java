package com.mello.repository;

import com.mello.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
// NOURHAN

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(String userId);
    List<Notification> findByUserIdAndStatusOrderByCreatedAtDesc(String userId, String status);

    Optional<Notification> findByUserIdAndEventKey(String userId, String eventKey);

    List<Notification> findByStatusAndScheduledForLessThanEqual(String status, LocalDateTime now);
}
