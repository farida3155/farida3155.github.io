import React, { useState, useEffect } from "react";
import { updateHabit, logHabitActivity } from "../controllers/HabitController";

function LogActivityModal({ habit, onClose, onLogged }) {
  const [timeLeft, setTimeLeft] = useState(habit.durationMinutes * 60);
  const [isActive, setIsActive] = useState(false);
  const [isCompleted, setIsCompleted] = useState(false);

  useEffect(() => {
    let interval = null;
    if (isActive && timeLeft > 0) {
      interval = setInterval(() => {
        setTimeLeft((time) => time - 1);
      }, 1000);
    } else if (timeLeft === 0 && !isCompleted) {
      handleComplete();
    }
    return () => clearInterval(interval);
  }, [isActive, timeLeft]);

  const handleComplete = async () => {
    setIsActive(false);
    setIsCompleted(true);

    // Increment count
    const nextCount = (habit.currentCount || 0) + 1;
    const target = habit.targetCount || 1;

    const isGoalReached = nextCount >= target;
    const today = new Date().getDay();
    const index = today === 0 ? 6 : today - 1;
    const alreadyDoneToday = habit.completedDays[index];

    let updatedCompletedDays = [...habit.completedDays];
    if (isGoalReached) {
      updatedCompletedDays[index] = true;
    }


    const updatedHabit = {
      ...habit,
      completedDays: updatedCompletedDays,
      currentCount: nextCount,
      progress: Math.min(100, Math.round((nextCount / target) * 100)),
      streak: (isGoalReached && !alreadyDoneToday) ? (habit.streak || 0) + 1 : (habit.streak || 0)
    };



    await updateHabit(habit.id, updatedHabit);
    
    // Create a log entry
    await logHabitActivity({
      habitId: habit.id,
      userId: habit.userId,
      habitName: habit.name,
      status: "COMPLETED"
    });

    if (onLogged) onLogged(isGoalReached);
  };

  const formatTime = (seconds) => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins}:${secs.toString().padStart(2, '0')}`;
  };

  const progressPercent = ((habit.durationMinutes * 60 - timeLeft) / (habit.durationMinutes * 60)) * 100;

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal timer-modal" onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <h2>Log: {habit.name}</h2>
          <button className="close-btn" onClick={onClose}>&times;</button>
        </div>

        <div className="modal-body timer-body">
          <div className="timer-circle-container">
            <svg className="timer-svg" viewBox="0 0 100 100">
              <circle className="timer-bg" cx="50" cy="50" r="45" />
              <circle
                className="timer-progress"
                cx="50"
                cy="50"
                r="45"
                style={{ strokeDashoffset: 283 - (283 * progressPercent) / 100 }}
              />
            </svg>
            <div className="timer-display">
              <span className="time">{formatTime(timeLeft)}</span>
              <span className="label">{isActive ? "Session in progress" : "Ready to start?"}</span>
            </div>
          </div>

          <div className="timer-controls">
            {!isCompleted ? (
              <>
                <button
                  className={`timer-btn ${isActive ? 'pause' : 'start'}`}
                  onClick={() => setIsActive(!isActive)}
                >
                  {isActive ? "Pause" : "Start Session"}
                </button>
                <button className="timer-btn skip" onClick={handleComplete}>
                  Mark as Done
                </button>
              </>
            ) : (
              <div className="completion-message">
                <div className="check-icon">✓</div>
                <h3>Session Done!</h3>
                <p>Great job! Your activity has been logged.</p>
                <button className="timer-btn start" onClick={onClose}>
                  Back to Dashboard
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default LogActivityModal;
