package com.mello.controller;

import com.mello.model.HabitLog;
import com.mello.service.HabitLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/habit-logs")
@CrossOrigin(origins = "*")
public class HabitLogController {
    @Autowired
    private HabitLogService habitLogService;

    @PostMapping
    public HabitLog createLog(@RequestBody HabitLog log) {
        return habitLogService.saveLog(log);
    }

    @GetMapping("/today")
    public List<HabitLog> getTodayLogs(@RequestParam String userId) {
        return habitLogService.getLogsForToday(userId);
    }
}
