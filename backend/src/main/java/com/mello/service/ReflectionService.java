package com.mello.service;

import com.mello.model.Reflection;
import com.mello.repository.ReflectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReflectionService {
    @Autowired
    private ReflectionRepository reflectionRepository;

    public List<Reflection> getAllReflections(String userId) {
        return reflectionRepository.findByUserId(userId, Sort.by(Sort.Direction.DESC, "timestamp"));
    }

    public Reflection saveReflection(Reflection reflection) {
        return reflectionRepository.save(reflection);
    }
}
