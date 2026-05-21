import React, { useState, useEffect, useMemo } from 'react';
import axios from 'axios';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  Filler
} from 'chart.js';
import { Line } from 'react-chartjs-2';
import { Heart, Trash2, Save, Flame, ArrowRight, Sparkles, Check, X } from 'lucide-react';

import Sidebar from "../components/Sidebar";
import WellnessCompanion from "../components/WellnessCompanion";
import { motion, AnimatePresence } from "framer-motion";

import "../styles/Mood.css";
import API_BASE from '../utils/api.js';

// Register ChartJS
ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  Filler
);

const API_BASE_URL = `${API_BASE}/api/moods`;

const MOOD_OPTIONS = [
  { id: 'happy', emoji: '😊', label: 'Happy', color: '#F9C97C' },
  { id: 'calm', emoji: '😌', label: 'Calm', color: '#A3D8A3' },
  { id: 'sad', emoji: '😔', label: 'Sad', color: '#B3A5D9' },
  { id: 'anxious', emoji: '😰', label: 'Anxious', color: '#E5A9A9' },
  { id: 'tired', emoji: '😴', label: 'Tired', color: '#BCB0E8' },
  { id: 'angry', emoji: '😡', label: 'Angry', color: '#FF7F7F' },
];

const PRACTICES = [
  { id: 'sleep', title: 'Better sleep', steps: ['Inhale quietly through your nose for 4 seconds.', 'Hold your breath for 7 seconds.', 'Exhale slowly through your mouth for 8 seconds.', 'Repeat 3–4 cycles until you feel calmer.'], guide: '4 - 7 - 8 Relaxation rhythm' },
  { id: 'anxiety', title: 'For anxiety', steps: ['Find a calm spot and take a few slow breaths.', 'Name 5 things you can see, 4 you can touch, 3 you can hear, 2 you can smell, and 1 you can taste.', 'Feel your body supported by the chair or floor.', 'Return gently to your breath whenever the mind wanders.'] },
  { id: 'morning', title: 'Morning meditations', steps: ['Sit comfortably with a straight spine.', 'Take 3 deep breaths and set a gentle intention.', 'Focus on your breath for 5–10 minutes.', 'Notice tension and release it with each exhale.'] },
  { id: 'focus', title: 'Mindfulness and concentration', steps: ['Choose an object, breath, or word to focus on.', 'Observe it gently without judgment.', 'If your thoughts wander, bring them back softly.', 'Practice for 3–5 minutes and notice your calm.'] },
];

const QUOTES = [
  "Your feelings are valid, and it's okay to not be okay.",
  "Be gentle with yourself. You're doing the best you can.",
  "Every day is a new beginning. Take a deep breath and start again.",
  "Self-care is not selfish. It's necessary for your well-being.",
  "Peace comes from within. Do not seek it without."
];

const Mood = () => {
  const [moodsHistory, setMoodsHistory] = useState([]);
  const [currentFilter, setCurrentFilter] = useState('week');
  const [activeMood, setActiveMood] = useState(null);
  const [expandedPractice, setExpandedPractice] = useState(null);
  const [quote, setQuote] = useState('');
  const [loading, setLoading] = useState(true);
  const [showSuccessPopUp, setShowSuccessPopUp] = useState(false);
  const [showWarningPopUp, setShowWarningPopUp] = useState(false);



  // Get user from localStorage
  const user = JSON.parse(localStorage.getItem('user'));
  const userId = user?.email; // Use email as the unique account identifier

  useEffect(() => {
    if (userId) {
      console.log('Mood tracker active for:', userId);
      fetchMoods();
    }
    setQuote(QUOTES[Math.floor(Math.random() * QUOTES.length)]);
  }, [userId]);

  const fetchMoods = async () => {
    if (!userId) return;
    console.log('Fetching moods for userId:', userId);
    try {
      const response = await axios.get(`${API_BASE_URL}?userId=${userId}`);
      setMoodsHistory(response.data.sort((a, b) => new Date(b.date) - new Date(a.date)));
    } catch (error) {
      console.error('Error fetching moods:', error);
      const stored = localStorage.getItem(`mello_moods_${userId}`);
      if (stored) setMoodsHistory(JSON.parse(stored));
    } finally {
      setLoading(false);
    }
  };

  const getTodayStr = () => {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  };

  const saveMoodForToday = async () => {
    if (!activeMood) {
      setShowWarningPopUp(true);
      return;
    }


    if (!userId) {
      alert('Please log in to save your mood');
      return;
    }

    const today = getTodayStr();
    const moodObj = MOOD_OPTIONS.find(m => m.id === activeMood);
    const moodEntry = { userId, date: today, mood: activeMood, emoji: moodObj.emoji };

    try {
      const response = await axios.post(API_BASE_URL, moodEntry);
      const savedEntry = response.data;

      const newHistory = [...moodsHistory];
      const existingIndex = newHistory.findIndex(entry => entry.date === today);
      if (existingIndex !== -1) newHistory[existingIndex] = savedEntry;
      else newHistory.push(savedEntry);

      setMoodsHistory(newHistory.sort((a, b) => new Date(b.date) - new Date(a.date)));
      localStorage.setItem(`mello_moods_${userId}`, JSON.stringify(newHistory));
      setShowSuccessPopUp(true);
    } catch (error) {
      console.error('Error saving mood:', error);
      alert('Failed to save to server, saved locally instead.');
    }
  };


  const deleteHistory = async () => {
    if (window.confirm('🌸 Clear all mood history?')) {
      try {
        await axios.delete(`${API_BASE_URL}?userId=${userId}`);
        setMoodsHistory([]);
        localStorage.removeItem(`mello_moods_${userId}`);
      } catch (error) {
        console.error('Error deleting moods:', error);
      }
    }
  };

  const filteredMoods = useMemo(() => {
    const now = new Date();
    let startDate = new Date();
    if (currentFilter === 'week') startDate.setDate(now.getDate() - 7);
    else if (currentFilter === 'month') startDate.setMonth(now.getMonth() - 1);
    else return moodsHistory;
    return moodsHistory.filter(entry => new Date(entry.date) >= startDate);
  }, [moodsHistory, currentFilter]);

  const streak = useMemo(() => {
    if (moodsHistory.length === 0) return 0;
    const sortedDates = [...new Set(moodsHistory.map(m => m.date))].sort();
    let currentStreak = 1;
    let maxStreak = 1;
    for (let i = 1; i < sortedDates.length; i++) {
      const prev = new Date(sortedDates[i - 1]);
      const curr = new Date(sortedDates[i]);
      const diffDays = (curr - prev) / (1000 * 60 * 60 * 24);
      if (diffDays === 1) currentStreak++;
      else currentStreak = 1;
      if (currentStreak > maxStreak) maxStreak = currentStreak;
    }
    return maxStreak;
  }, [moodsHistory]);

  const chartData = useMemo(() => {
    const sorted = [...filteredMoods].sort((a, b) => new Date(a.date) - new Date(b.date));
    const labels = sorted.map(e => e.date.slice(5));
    const moodValueMap = { happy: 5, calm: 4, sad: 2, anxious: 1.5, tired: 1.8, angry: 1.2 };

    return {
      labels,
      datasets: [{
        label: 'Mood balance',
        data: sorted.map(e => moodValueMap[e.mood] || 3),
        borderColor: '#b38fdd',
        backgroundColor: 'rgba(179, 143, 221, 0.1)',
        tension: 0.2,
        fill: true,
        pointBackgroundColor: sorted.map(e => MOOD_OPTIONS.find(m => m.id === e.mood)?.color || '#BCB0E8'),
        pointRadius: 5,
      }]
    };
  }, [filteredMoods]);

  const calendarDays = useMemo(() => {
    const now = new Date();
    const year = now.getFullYear();
    const month = now.getMonth();
    const firstDay = new Date(year, month, 1).getDay();
    const daysInMonth = new Date(year, month + 1, 0).getDate();

    const days = [];
    for (let i = 0; i < firstDay; i++) days.push({ type: 'empty' });
    for (let d = 1; d <= daysInMonth; d++) {
      const dateStr = `${year}-${String(month + 1).padStart(2, '0')}-${String(d).padStart(2, '0')}`;
      const mood = moodsHistory.find(m => m.date === dateStr);
      days.push({
        type: 'day',
        day: d,
        date: dateStr,
        mood: mood,
        isToday: d === now.getDate() && month === now.getMonth() && year === now.getFullYear()
      });
    }
    return days;
  }, [moodsHistory]);

  return (
    <div className="dashboard habits mood-page">
      <div className="app-container habit-pure-page">
        <Sidebar />

        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          className="content-area no-scrollbar"
        >
          <div className="wellness-page-header">
            <div className="header-left-group">
              <div className="buddy-figure">
                <img src="/mello_bud.png" alt="Mello Buddy" className="buddy-img" />
              </div>
              <div className="header-content">
                <motion.div
                  initial={{ y: -20, opacity: 0 }}
                  animate={{ y: 0, opacity: 1 }}
                  transition={{ delay: 0.2 }}
                  className="companion-wrapper-top"
                >
                </motion.div>
                <h1>Mood Tracker</h1>
                <p>Hello Mello · Take care of your mind today. 🌸</p>
              </div>
            </div>

            <div className="wellness-stats">
              <div className="badge-date" style={{ height: 'fit-content' }}>
                {new Date().toLocaleDateString(undefined, { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })}
              </div>
            </div>
          </div>

          <div className="wellness-main-view">
            <div className="moodtrack">

              <div className="dashboard-grid top-row">
                <div className="card mood-input-card">
                  <div className="card-title">
                    <img src="/mello_bud.png" alt="Mello Buddy" className="buddy-img" style={{ width: '40px', height: '40px', objectFit: 'contain' }} />
                    What do you feel today?
                  </div>
                  <i>Your feelings matter, let's track them</i>
                  <div className="mood-grid">
                    {MOOD_OPTIONS.map(mood => (
                      <div
                        key={mood.id}
                        className={`mood-option ${activeMood === mood.id ? 'active' : ''}`}
                        data-mood={mood.id}
                        onClick={() => setActiveMood(mood.id)}
                      >
                        <span>{mood.emoji}</span>
                        <span className="mood-label">{mood.label}</span>
                      </div>
                    ))}
                  </div>
                  <button onClick={saveMoodForToday} style={{ marginTop: 15, width: '100%' }}>
                    <Save size={18} style={{ marginRight: 8 }} /> Save mood & track
                  </button>
                  <div className="insight-box">
                    <Flame size={16} style={{ marginRight: 8 }} /> Current streak: {streak} days of mindful tracking!
                  </div>
                </div>

                <div className="card">
                  <h2 className="card-title">
                    <img src="/mello_bud.png" alt="Mello Buddy" className="buddy-img" style={{ width: '40px', height: '40px', objectFit: 'contain' }} />
                    Mood history & analytics
                  </h2>
                  <div className="filter-group">
                    {['week', 'month', 'all'].map(f => (
                      <button
                        key={f}
                        className={currentFilter === f ? 'active-filter' : ''}
                        onClick={() => setCurrentFilter(f)}
                      >
                        {f === 'week' ? 'This week' : f === 'month' ? 'This month' : 'All time'}
                      </button>
                    ))}
                  </div>
                  <div style={{ height: 200 }}>
                    <Line data={chartData} options={{ responsive: true, maintainAspectRatio: false }} />
                  </div>
                  <div className="mood-history-list">
                    {filteredMoods.length === 0 ? (
                      <div style={{ textAlign: 'center', padding: 20 }}>✨ No moods recorded. Save your first mood!</div>
                    ) : (
                      filteredMoods.map((entry, i) => (
                        <div key={i} className="history-entry">
                          <span><strong>{entry.date}</strong> {entry.emoji} {entry.mood.charAt(0).toUpperCase() + entry.mood.slice(1)}</span>
                          <span className="mood-emoji-history">{entry.emoji}</span>
                        </div>
                      ))
                    )}
                  </div>
                  <button className="delete-history-btn" onClick={deleteHistory}>
                    <Trash2 size={16} style={{ marginRight: 8 }} /> Delete all mood history
                  </button>
                </div>
              </div>

              <div className="dashboard-grid meditation-row">
                <div className="card recommendations-card" style={{ background: '#e9f0ef', border: 'none', boxShadow: 'none' }}>
                  <div className="card-title" style={{ justifyContent: 'center', borderLeft: 'none', paddingLeft: 0, fontSize: '1.5rem', color: '#2d3b36' }}>
                    Meditation
                  </div>

                  <div className="section-title">Try today</div>
                  <div className="try-today-box white-card" style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
                      <img src="/mello_bud.png" alt="Mello Buddy" className="buddy-img" style={{ width: '40px', height: '40px', objectFit: 'contain' }} />
                      <div className="try-today-content">
                        <div className="try-today-title">Morning meditation</div>
                        <div className="try-today-desc">Start your day with a short meditation.<br /><span className="try-today-time">10 minutes</span></div>
                      </div>
                    </div>
                  </div>

                  <div style={{ position: 'relative', marginTop: 24 }}>
                    <div className="section-title">Practices</div>
                    <div className="practices-list white-card">
                      {PRACTICES.map(p => (
                        <React.Fragment key={p.id}>
                          <div
                            className={`practice-option ${expandedPractice === p.id ? 'selected' : ''}`}
                            onClick={() => setExpandedPractice(expandedPractice === p.id ? null : p.id)}
                          >
                            <span>{p.title}</span>
                            <ArrowRight size={16} />
                          </div>
                          {expandedPractice === p.id && (
                            <div className="practice-exercises">
                              <div className="exercise-title"><strong>{p.title}</strong></div>
                              <div className="exercise-content">
                                <ol style={{ marginLeft: 20, marginTop: 8 }}>
                                  {p.steps.map((step, i) => <li key={i}>{step}</li>)}
                                </ol>
                              </div>
                              {p.guide && (
                                <div className="breathing-guide" style={{ marginTop: 12, display: 'flex', alignItems: 'center', gap: 12, background: '#eef5f3', padding: 10, borderRadius: 8 }}>
                                  <div className="breath-circle" style={{ background: '#8b6bbd', color: 'white', width: 60, height: 60, borderRadius: '50%', display: 'flex', alignItems: 'center', justifyContent: 'center', fontWeight: 'bold' }}>4-7-8</div>
                                  <div>
                                    <p><strong>{p.guide}</strong></p>
                                    <p style={{ color: '#507d9d', fontSize: '0.85rem' }}>Inhale 4s • Hold 7s • Exhale 8s</p>
                                  </div>
                                </div>
                              )}
                            </div>
                          )}
                        </React.Fragment>
                      ))}
                    </div>
                  </div>
                </div>
              </div>

              <div className="dashboard-grid">
                <div className="card calendar-card">
                  <div className="calendar-header">
                    <div className="calendar-title">
                      <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
                        <img src="/mello_bud.png" alt="Mello Buddy" className="buddy-img" style={{ width: '40px', height: '40px', objectFit: 'contain' }} />
                        Your Mood Calendar
                      </div>
                    </div>
                    <div className="calendar-month-year" style={{ background: '#f5eefc', padding: '6px 12px', borderRadius: 20 }}>
                      {new Date().toLocaleDateString(undefined, { month: 'long', year: 'numeric' })}
                    </div>
                  </div>
                  <div className="calendar-days-header" style={{ display: 'grid', gridTemplateColumns: 'repeat(7, 1fr)', textAlign: 'center', fontSize: '0.8rem', fontWeight: 600, color: '#a38bbb', marginBottom: 12 }}>
                    {['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'].map(d => <div key={d}>{d}</div>)}
                  </div>
                  <div className="calendar-grid">
                    {calendarDays.map((d, i) => (
                      <div
                        key={i}
                        className={`calendar-day ${d.type === 'empty' ? 'empty' : ''} ${d.isToday ? 'today' : ''} ${d.mood ? `mood-recorded mood-${d.mood.mood}` : ''}`}
                      >
                        {d.type === 'day' ? (d.mood ? d.mood.emoji : d.day) : ''}
                      </div>
                    ))}
                  </div>
                  <div className="quote-area" style={{ background: '#ede4fc', padding: 16, borderRadius: 24, textAlign: 'center', fontStyle: 'italic', color: '#4a2d6b' }}>
                    "{quote}"
                  </div>
                </div>
              </div>
              <footer>🧘‍♀️ Mello — track your mood, nurture your mind, find your calm. 💜</footer>
            </div>
          </div>
        </motion.div>

        <AnimatePresence>
          {showSuccessPopUp && (
            <motion.div
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              className="mood-success-overlay"
            >
              <motion.div
                initial={{ scale: 0.8, y: 20 }}
                animate={{ scale: 1, y: 0 }}
                exit={{ scale: 0.8, y: 20 }}
                className="mood-success-popup"
              >
                <div className="popup-companion">
                  <img src="/small-cute-character.svg" alt="Mello" />
                </div>
                <div className="popup-content">
                  <div className="heart-icon-badge">
                    <Heart size={32} fill="var(--color-primary)" color="var(--color-primary)" />
                  </div>
                  <h2>Mood Tracked!</h2>
                  <p>Your feelings have been safely tucked away. Take a deep breath and stay kind to yourself. 🌸</p>
                  <button className="continue-btn" onClick={() => setShowSuccessPopUp(false)}>
                    Continue <ArrowRight size={18} />
                  </button>
                </div>
                <button className="close-popup-icon" onClick={() => setShowSuccessPopUp(false)}>
                  <X size={20} />
                </button>
              </motion.div>
            </motion.div>
          )}

          {showWarningPopUp && (
            <motion.div
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              className="mood-success-overlay"
            >
              <motion.div
                initial={{ scale: 0.8, y: 20 }}
                animate={{ scale: 1, y: 0 }}
                exit={{ scale: 0.8, y: 20 }}
                className="mood-success-popup warning"
              >
                <div className="popup-companion">
                  <img src="src/assets/icons/small-cute-character.svg" alt="Mello" />
                </div>
                <div className="popup-content">
                  <div className="heart-icon-badge warning">
                    <Sparkles size={32} color="#8A64D6" />
                  </div>
                  <h2>Wait a moment...</h2>
                  <p>Please select a mood that best describes how you're feeling before tracking. 💜</p>
                  <button className="continue-btn" onClick={() => setShowWarningPopUp(false)}>
                    Got it!
                  </button>
                </div>
                <button className="close-popup-icon" onClick={() => setShowWarningPopUp(false)}>
                  <X size={20} />
                </button>
              </motion.div>
            </motion.div>
          )}
        </AnimatePresence>

      </div>
    </div>

  );
};

export default Mood;
