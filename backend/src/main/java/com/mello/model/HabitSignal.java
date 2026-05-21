package com.mello.model;

public class HabitSignal {
    private final String habitName;
    private final double completionRate;
    private final int streakDays;
    private final int lastCompletedDaysAgo;

    public HabitSignal(String habitName, double completionRate, int streakDays, int lastCompletedDaysAgo) {
        this.habitName = habitName;
        this.completionRate = completionRate;
        this.streakDays = streakDays;
        this.lastCompletedDaysAgo = lastCompletedDaysAgo;
    }

    public String getHabitName() {
        return habitName;
    }

    public double getCompletionRate() {
        return completionRate;
    }

    public int getStreakDays() {
        return streakDays;
    }

    public int getLastCompletedDaysAgo() {
        return lastCompletedDaysAgo;
    }
}
