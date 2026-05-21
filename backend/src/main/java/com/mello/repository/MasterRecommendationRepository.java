package com.mello.repository;

import com.mello.model.MasterRecommendation;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MasterRecommendationRepository extends MongoRepository<MasterRecommendation, String> {
    List<MasterRecommendation> findByMoodCategory(String moodCategory);
    List<MasterRecommendation> findByMoodCategoryAndType(String moodCategory, String type);
}
