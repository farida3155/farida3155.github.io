import React from "react";

function CelebrationModal({ habit, onClose }) {
  return (
    <div className="modal-overlay celebration-overlay" onClick={onClose}>
      <div className="modal celebration-modal" onClick={e => e.stopPropagation()}>
        <div className="confetti-container">
          {[...Array(20)].map((_, i) => (
            <div key={i} className={`confetti c${(i % 12) + 1}`}></div>
          ))}
        </div>
        
        <div className="celebration-content">
          <div className="celebration-icon">🥳</div>
          <h2>Goal Crushed!</h2>
          <p>You've completed your <strong>{habit.name}</strong> goal for today! ✨</p>
          <div className="stats-mini">
            <span>Progress reset for next time! 🔄</span>
          </div>
          <button className="timer-btn start celebration-btn" onClick={onClose}>
            Keep it up! 🚀
          </button>
        </div>
      </div>
    </div>
  );
}

export default CelebrationModal;
