import React, { useState, useEffect } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { Plus } from "lucide-react";
import Sidebar from "../components/Sidebar";
import HabitCard from "../components/HabitCard";
import AddHabitModal from "../components/AddHabitModel";
import LogActivityModal from "../components/LogActivityModal";
import CelebrationModal from "../components/CelebrationModal";
import ConfirmDeleteModal from "../components/ConfirmDeleteModal";
import WellnessCompanion from "../components/WellnessCompanion";
import { fetchHabits, deleteHabit } from "../controllers/HabitController";
import "../styles/habit.css";

function HabitManagement() {
  const [user, setUser] = useState(null);
  const [habits, setHabits] = useState([]);
  const [showAddModal, setShowAddModal] = useState(false);
  const [showLogModal, setShowLogModal] = useState(false);
  const [showCelebration, setShowCelebration] = useState(false);
  const [showConfirmDelete, setShowConfirmDelete] = useState(false);
  const [selectedHabit, setSelectedHabit] = useState(null);
  const [habitToDelete, setHabitToDelete] = useState(null);

  useEffect(() => {
    const savedUser = localStorage.getItem('user');
    if (savedUser) {
      const parsedUser = JSON.parse(savedUser);
      setUser(parsedUser);
      loadHabits(parsedUser.id);
    }
  }, []);

  const loadHabits = async (userId) => {
    if (!userId) return;
    const data = await fetchHabits(userId);
    setHabits([...data].reverse());
  };

  const handleConfirmDelete = async () => {
    if (habitToDelete) {
      const success = await deleteHabit(habitToDelete.id);
      if (success) {
        setShowConfirmDelete(false);
        setHabitToDelete(null);
        loadHabits();
      }
    }
  };

  const handleLogComplete = (isGoalReached) => {
    setShowLogModal(false);
    loadHabits();
    if (isGoalReached) {
      setTimeout(() => setShowCelebration(true), 300);
    }
  };


  return (
    <div className="dashboard habits">
      <div className="app-container habit-pure-page">
        <Sidebar />

        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          className="content-area no-scrollbar"
        >
          <div className="wellness-page-header">
            <div className="header-left-group">
              <div className="header-companion">
                <WellnessCompanion />
              </div>
              <div className="header-content">
                <motion.div
                  initial={{ y: -20, opacity: 0 }}
                  animate={{ y: 0, opacity: 1 }}
                  transition={{ delay: 0.2 }}
                  className="companion-wrapper-top"
                >
                </motion.div>
                <h1>Habit Tracker</h1>
                <p>Small steps matter. Let's make today count.</p>
              </div>
            </div>

            <div className="wellness-stats">

              <div className="add-habit-wrapper">
                <motion.div
                  className="button-mascot-lean"
                  initial={{ y: 10, opacity: 0 }}
                  animate={{ y: 0, opacity: 1 }}
                  transition={{ delay: 0.5 }}
                >
                  <img src="/kotyy.png" alt="Koty Mascot" />
                </motion.div>
                <motion.button
                  whileHover={{ scale: 1.05 }}
                  whileTap={{ scale: 0.95 }}
                  className="plant-new-btn"
                  onClick={() => setShowAddModal(true)}
                >
                  <Plus size={20} />
                  Add New Habit
                </motion.button>
              </div>
            </div>
          </div>

          <div className="wellness-main-view">
            <div className="habit-tracking-section">
              <div className="section-header-pastel">
                <h2>All Habits</h2>
                <div className="pastel-accent-line" />
              </div>

              <motion.div
                layout
                className="habits-wellness-grid"
              >
                <AnimatePresence>
                  {habits.map((habit) => (
                    <HabitCard
                      key={habit.id}
                      habit={habit}
                      onDelete={(id) => {
                        const h = habits.find(h => h.id === id);
                        setHabitToDelete(h);
                        setShowConfirmDelete(true);
                      }}
                      onLog={(h) => {
                        setSelectedHabit(h);
                        setShowLogModal(true);
                      }}
                    />
                  ))}
                </AnimatePresence>
              </motion.div>
            </div>
          </div>
        </motion.div>

        {showAddModal && (
          <AddHabitModal
            onClose={() => setShowAddModal(false)}
            onHabitAdded={() => loadHabits(user.id)}
            userId={user?.id}
          />
        )}

        {showLogModal && selectedHabit && (
          <LogActivityModal
            habit={selectedHabit}
            onClose={() => setShowLogModal(false)}
            onLogged={handleLogComplete}
          />
        )}

        {showCelebration && selectedHabit && (
          <CelebrationModal
            habit={selectedHabit}
            onClose={() => setShowCelebration(false)}
          />
        )}

        {showConfirmDelete && habitToDelete && (
          <ConfirmDeleteModal
            habitName={habitToDelete.name}
            onConfirm={handleConfirmDelete}
            onCancel={() => setShowConfirmDelete(false)}
          />
        )}
      </div>
    </div>
  );
}

export default HabitManagement;