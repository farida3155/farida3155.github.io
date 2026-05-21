package com.mello.repository;

import com.mello.model.Mood;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MoodRepository extends MongoRepository<Mood, String> {
    @Query("{ 'userId': ?0, 'date': ?1 }")
    Optional<Mood> findByUserIdAndDate(String userId, String date);

    @Query("{ 'userId': ?0 }")
    List<Mood> findByUserIdOrderByDateDesc(String userId);

    @Query("{ 'userId': ?0 }")
    List<Mood> findByUserId(String userId);

    @Query(value = "{ 'userId': ?0 }", delete = true)
    void deleteByUserId(String userId);

    @Query(value = "{ 'userId': ?0, 'date': ?1 }", exists = true)
    boolean existsByUserIdAndDate(String userId, String date);
}
