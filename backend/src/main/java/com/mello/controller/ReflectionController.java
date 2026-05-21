package com.mello.controller;

import com.mello.model.Reflection;
import com.mello.service.ReflectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reflections")
@CrossOrigin(origins = "*")
public class ReflectionController {


    @Autowired
    private ReflectionService reflectionService;

    @GetMapping
    public List<Reflection> getAllReflections(@RequestParam String userId) {
        return reflectionService.getAllReflections(userId);
    }

    @PostMapping
    public Reflection createReflection(@RequestBody Reflection reflection) {
        return reflectionService.saveReflection(reflection);
    }
}
