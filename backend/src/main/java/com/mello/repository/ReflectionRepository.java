package com.mello.repository;

import com.mello.model.Reflection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReflectionRepository extends MongoRepository<Reflection, String> {
    List<Reflection> findByUserId(String userId, Sort sort);
}
