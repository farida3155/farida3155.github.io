import React, { useState } from "react";
import { createHabit } from "../controllers/HabitController";

function AddHabitModal({ onClose, onHabitAdded, userId }) {
  const [habitName, setHabitName] = useState("");

  // New fields
  const [activeDays, setActiveDays] = useState([]);
  const [durationMinutes, setDurationMinutes] = useState(15);
  const [reminderTime, setReminderTime] = useState("08:00");
  const [periodicity, setPeriodicity] = useState("weekly");
  const [targetCount, setTargetCount] = useState(1);

  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");

  const weekDays = [
    { label: "M", value: "MON" },
    { label: "T", value: "TUE" },
    { label: "W", value: "WED" },
    { label: "T", value: "THU" },
    { label: "F", value: "FRI" },
    { label: "S", value: "SAT" },
    { label: "S", value: "SUN" }
  ];

  const durationOptions = [5, 10, 15, 30, 60];

  const toggleDay = (dayValue) => {
    if (activeDays.includes(dayValue)) {
      setActiveDays(activeDays.filter(d => d !== dayValue));
    } else {
      setActiveDays([...activeDays, dayValue]);
    }
  };

  const handleSubmit = async () => {
    setError("");
    if (!habitName.trim()) {
      setError("Habit name is required");
      return;
    }
    if (periodicity === "weekly" && activeDays.length === 0) {
      setError("Please select at least one active day");
      return;
    }

    setIsLoading(true);

    const newHabit = {
      userId,
      name: habitName,
      completedDays: [false, false, false, false, false, false, false],
      progress: 0,
      activeDays: periodicity === "weekly" ? activeDays : [],
      durationMinutes,
      reminderTime,
      periodicity,
      targetCount: periodicity === "weekly" ? activeDays.length : targetCount,
      currentCount: 0
    };

    const result = await createHabit(newHabit);

    setIsLoading(false);

    if (result) {
      if (onHabitAdded) onHabitAdded();
      onClose();
    } else {
      setError("Failed to add habit. Please try again.");
    }
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <h2>Create New Habit</h2>
          <button className="close-btn" onClick={onClose}>&times;</button>
        </div>

        <div className="modal-body custom-scroll">
          <div className="input-group">
            <label>Habit Name</label>
            <input
              type="text"
              value={habitName}
              onChange={(e) => { setHabitName(e.target.value); setError(""); }}
              className={error && !habitName.trim() ? "input-error" : ""}
              disabled={isLoading}
            />
          </div>

          <div className="input-group">
            <label>Frequency</label>
            <div className="period-selector">
              {["daily", "weekly", "monthly"].map(p => (
                <button
                  key={p}
                  className={`period-btn ${periodicity === p ? "active" : ""}`}
                  onClick={() => {
                    setPeriodicity(p);
                    setError("");
                  }}
                >
                  {p.charAt(0).toUpperCase() + p.slice(1)}
                </button>
              ))}
            </div>
          </div>

          {periodicity === "weekly" ? (
            <div className="input-group">
              <label>Repeat on</label>
              <div className="days-picker">
                {weekDays.map((day, idx) => (
                  <div
                    key={idx}
                    className={`day-pill ${activeDays.includes(day.value) ? "active" : ""}`}
                    onClick={() => toggleDay(day.value)}
                  >
                    {day.label}
                  </div>
                ))}
              </div>
            </div>
          ) : (
            <div className="input-group">
              <label>How many times per {periodicity === "daily" ? "day" : "month"}?</label>
              <input
                type="number"
                min="1"
                max={periodicity === "daily" ? 10 : 31}
                value={targetCount}
                onChange={(e) => setTargetCount(parseInt(e.target.value) || 1)}
                className="number-input"
              />
            </div>
          )}

          <div className="row-group">
            <div className="input-group flex-1">
              <label>Duration</label>
              <div className="duration-picker">
                {durationOptions.map(mins => (
                  <div
                    key={mins}
                    className={`duration-chip ${durationMinutes === mins ? "active" : ""}`}
                    onClick={() => setDurationMinutes(mins)}
                  >
                    {mins >= 60 ? `${mins / 60}h` : `${mins}m`}
                  </div>
                ))}
              </div>
            </div>

            <div className="input-group flex-1">
              <label>Reminder</label>
              <input
                type="time"
                className="time-picker"
                value={reminderTime}
                onChange={(e) => setReminderTime(e.target.value)}
              />
            </div>
          </div>

          {error && <div className="error-message">{error}</div>}
        </div>

        <div className="modal-buttons">
          <button className="btn-cancel" onClick={onClose} disabled={isLoading}>Cancel</button>
          <button
            className={`btn-add ${isLoading ? 'loading' : ''}`}
            onClick={handleSubmit}
            disabled={isLoading || !habitName.trim()}
          >
            {isLoading ? "Saving..." : "Save Habit"}
          </button>
        </div>
      </div>
    </div>
  );
}

export default AddHabitModal;