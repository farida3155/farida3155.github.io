package com.mello.repository;

import com.mello.model.RecommendationEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface RecommendationRepository extends MongoRepository<RecommendationEntry, String> {
    List<RecommendationEntry> findByUserId(String userId);
}
