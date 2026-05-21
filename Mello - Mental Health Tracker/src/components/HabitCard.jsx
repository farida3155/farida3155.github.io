import React from "react";
import { motion } from "framer-motion";
import { Clock, RefreshCw, Trash2, Sparkles, Star } from "lucide-react";

function HabitCard({ habit, onDelete, onLog }) {
  const weekDays = ["M", "T", "W", "T", "F", "S", "S"];

  return (
    <motion.div
      layout
      initial={{ opacity: 0, scale: 0.9 }}
      animate={{ opacity: 1, scale: 1 }}
      exit={{ opacity: 0, scale: 0.8 }}
      whileHover={{ y: -12, scale: 1.02 }}
      className="habit-card pastel-card"
    >
      <div className="habit-header">
        <div className="habit-info">
          <h3>{habit.name}</h3>
          <p>{habit.description}</p>
        </div>

        <div className="streak-badge">
          <motion.div
            animate={{ rotate: [0, 10, -10, 0] }}
            transition={{ repeat: Infinity, duration: 2 }}
          >
            <Star size={18} color="#E0BBE4" fill="#E0BBE4" />
          </motion.div>
          <span>{habit.streak || 0}d</span>
        </div>
      </div>

      <div className="habit-meta-pastel">
        {habit.durationMinutes && (
          <span className="badge-lilac">
            <Clock size={12} /> {habit.durationMinutes}m
          </span>
        )}
        <span className="badge-mint">
          <RefreshCw size={12} /> {habit.periodicity}
        </span>
      </div>

      <div className="week-tracker-pastel">
        {weekDays.map((day, index) => {
          const isDone = habit.completedDays[index];
          return (
            <div key={index} className="day-bubble-wrapper">
              <span>{day}</span>
              <motion.button
                whileHover={{ scale: 1.2 }}
                whileTap={{ scale: 0.9 }}
                className={`day-bubble ${isDone ? "active" : ""}`}
                onClick={() => onLog(habit)}
              >
                {isDone && <motion.div initial={{ scale: 0 }} animate={{ scale: 1 }} className="dot" />}
              </motion.button>
            </div>
          );
        })}
      </div>

      <div className="progress-container-pastel">
        <div className="progress-track">
          <motion.div
            initial={{ width: 0 }}
            animate={{ width: `${habit.progress || 0}%` }}
            className="progress-fill-lavender"
          />
        </div>
        <div className="progress-info">
          <span>{habit.progress || 0}% completed ({habit.currentCount || 0} of {habit.targetCount || 1})</span>

          {(habit.progress || 0) >= 100 && <Sparkles size={14} className="sparkle-icon" />}
        </div>
      </div>

      <div className="habit-card-actions">
        <motion.button
          whileHover={{ scale: 1.05 }}
          whileTap={{ scale: 0.95 }}
          className="log-btn-pastel"
          onClick={() => onLog(habit)}
        >
          Log Progress ✨
        </motion.button>
        <motion.button
          whileHover={{ scale: 1.1, color: "#FFB3B3" }}
          className="delete-btn-ghost"
          onClick={() => onDelete(habit.id)}
        >
          <Trash2 size={18} />
        </motion.button>
      </div>
    </motion.div>
  );
}

export default HabitCard;