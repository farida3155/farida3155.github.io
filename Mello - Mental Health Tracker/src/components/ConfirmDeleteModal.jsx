import React from "react";

function ConfirmDeleteModal({ habitName, onConfirm, onCancel }) {
  return (
    <div className="modal-overlay" onClick={onCancel}>
      <div className="modal confirm-modal" onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <h2>Delete Habit?</h2>
          <button className="close-btn" onClick={onCancel}>&times;</button>
        </div>
        
        <div className="modal-body confirm-body">
          <div className="warning-icon">⚠️</div>
          <p>Are you sure you want to delete <strong>{habitName}</strong>?</p>
          <p className="sub-text">This action cannot be undone and all your progress for this habit will be lost.</p>
        </div>

        <div className="modal-buttons confirm-buttons">
          <button className="btn-cancel" onClick={onCancel}>
            Keep it
          </button>
          <button className="btn-delete-confirm" onClick={onConfirm}>
            Yes, Delete
          </button>
        </div>
      </div>
    </div>
  );
}

export default ConfirmDeleteModal;
