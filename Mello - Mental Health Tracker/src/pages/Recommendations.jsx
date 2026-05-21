import React, { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { Sparkles, CheckCircle2, Zap, Heart, MessageCircle, Wind } from 'lucide-react';
import Sidebar from '../components/Sidebar';
import RecommendationActionModal from '../components/RecommendationActionModal';
import { fetchRecommendations } from '../services/recommendationService';
import '../styles/recommendations.css';

function Recommendations() {
  const [recommendations, setRecommendations] = useState(null);
  const [selectedMood, setSelectedMood] = useState('neutral');
  const [loading, setLoading] = useState(true);
  const [user, setUser] = useState(null);
  const [activeAction, setActiveAction] = useState(null);

  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        staggerChildren: 0.1
      }
    }
  };

  const itemVariants = {
    hidden: { x: -20, opacity: 0 },
    visible: { x: 0, opacity: 1 }
  };


  useEffect(() => {
    const savedUser = localStorage.getItem('user');
    if (savedUser) {
      const parsedUser = JSON.parse(savedUser);
      setUser(parsedUser);
      loadRecommendations('neutral', parsedUser.id);
    }
  }, []);

  const loadRecommendations = async (mood, userId) => {
    setLoading(true);
    const data = await fetchRecommendations(mood, userId);
    if (data) {
      setRecommendations(data);
    }
    setLoading(false);
  };

  const handleMoodChange = (newMood) => {
    setSelectedMood(newMood);
    if (user) {
      loadRecommendations(newMood, user.id);
    }
  };

  const moodOptions = [
    { id: 'low', label: 'Low', emoji: '😔' },
    { id: 'neutral', label: 'Neutral', emoji: '😐' },
    { id: 'stressed', label: 'Stressed', emoji: '😰' },
    { id: 'happy', label: 'Happy', emoji: '😊' },
  ];

  const handleStartAction = (item) => {
    const title = item.title.toLowerCase();
    if (title.includes('walk')) {
      setActiveAction({ type: 'timer', duration: 5, title: item.title });
    } else if (title.includes('reflection')) {
      setActiveAction({ type: 'reflection' });
    } else if (title.includes('text someone') || title.includes('message someone')) {
      setActiveAction({ type: 'message-love' });
    } else if (title.includes('step outside') || title.includes('fresh air')) {
      setActiveAction({ type: 'garden-stopwatch' });
    } else if (title.includes('breathing') || title.includes('breaths')) {
      setActiveAction({ type: 'breathing' });
    } else if (title.includes('relax your shoulders') || title.includes('jaw')) {
      setActiveAction({ type: 'relaxation' });
    } else if (title.includes('pressure release')) {
      setActiveAction({ type: 'pressure' });
    } else if (title.includes('nature sounds') || title.includes('listen to')) {
      setActiveAction({ type: 'nature-sounds' });
    } else {
      alert('Enjoy this moment!');
    }
  };

  return (
    <div className="recommendations-page dashboard">
      <div className="app-container">
        <Sidebar />

        <div className="content-area no-scrollbar">
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="top-header"
          >
            <div>
              <h1>Personalized Recommendations</h1>
              <p>Small steps tailored to your current energy.</p>
            </div>
          </motion.div>

          <div className="mood-selection-container glass-panel">
            <div className="mood-filter">
              <h2>How are you feeling right now?</h2>
              <p className="mood-help">Your recommendations will adapt to support your mood.</p>
              <div className="mood-chip-form">
                {moodOptions.map((mood) => (
                  <button
                    key={mood.id}
                    onClick={() => handleMoodChange(mood.id)}
                    className={`mood-chip ${selectedMood === mood.id ? 'active' : ''}`}
                  >
                    <span className="mood-emoji">{mood.emoji}</span>
                    <span>{mood.label}</span>
                  </button>
                ))}
              </div>
            </div>

            {recommendations?.gentleRiskAlert && (
              <motion.div
                initial={{ opacity: 0, scale: 0.95 }}
                animate={{ opacity: 1, scale: 1 }}
                className="dynamic-note-section"
              >
                <div className="note-icon">
                  <Sparkles size={20} />
                </div>
                <div className="note-content">
                  <h4>Personal Insight</h4>
                  <p>{recommendations.gentleRiskAlert}</p>
                </div>
              </motion.div>
            )}
          </div>

          <AnimatePresence mode="wait">
            {loading ? (
              <motion.div
                key="loading"
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                exit={{ opacity: 0 }}
                className="loading-state"
              >
                <div className="pulse-loader"></div>
                <p>Curating your wellness path...</p>
              </motion.div>
            ) : (
              <motion.div
                key="content"
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                className="recommendations-content"
              >


                <div className="focus-section">
                  <h2>Today's Focus</h2>
                  <div className="focus-card">
                    <div className="focus-icon">
                      <Sparkles color="#8A64D6" size={24} />
                    </div>
                    <div>
                      <h3>Keep it simple</h3>
                      <p>Focus on one small action at a time. Progress is a series of tiny wins.</p>
                    </div>
                  </div>
                </div>

                <div className="support-grid">
                  <motion.div
                    variants={containerVariants}
                    initial="hidden"
                    animate="visible"
                    className="support-card"
                  >
                    <h2><Zap size={18} /> Smart Suggestions</h2>
                    <div className="practice-list">
                      {recommendations?.smartSuggestions?.map((item, idx) => (
                        <motion.div
                          key={idx}
                          variants={itemVariants}
                          whileHover={{ x: 10, backgroundColor: 'rgba(255,255,255,0.8)' }}
                          className="practice-row advanced-row"
                        >
                          <div className="practice-label">
                            <div className="emoji-wrapper">
                              <img src={item.emojiUrl} alt={item.emojiAlt} className="emoji-icon" />
                            </div>
                            <div className="practice-text">
                              <h3>{item.title}</h3>
                              {item.description && <p>{item.description}</p>}
                            </div>
                          </div>
                          <motion.button
                            whileHover={{ scale: 1.05 }}
                            whileTap={{ scale: 0.95 }}
                            className="done-btn"
                            onClick={() => handleStartAction(item)}
                          >
                            Start
                          </motion.button>
                        </motion.div>
                      ))}
                    </div>
                  </motion.div>

                  <motion.div
                    variants={containerVariants}
                    initial="hidden"
                    animate="visible"
                    className="support-card"
                  >
                    <h2><Wind size={18} /> Micro Actions</h2>
                    <div className="practice-list">
                      {recommendations?.microActions?.map((item, idx) => (
                        <motion.div
                          key={idx}
                          variants={itemVariants}
                          whileHover={{ x: 10, backgroundColor: 'rgba(255,255,255,0.8)' }}
                          className="practice-row advanced-row"
                        >
                          <div className="practice-label">
                            <div className="emoji-wrapper">
                              <img src={item.emojiUrl} alt={item.emojiAlt} className="emoji-icon" />
                            </div>
                            <div className="practice-text">
                              <h3>{item.title}</h3>
                              {item.description && <p>{item.description}</p>}
                            </div>
                          </div>
                          <motion.button
                            whileHover={{ scale: 1.05 }}
                            whileTap={{ scale: 0.95 }}
                            className="done-btn"
                            onClick={() => handleStartAction(item)}
                          >
                            Do it
                          </motion.button>
                        </motion.div>
                      ))}
                    </div>
                  </motion.div>
                </div>


                <div className="support-grid">
                  <div className="support-card">
                    <h2><CheckCircle2 size={18} /> Weekly Plan</h2>
                    <ul className="plan-list">
                      {recommendations?.weeklyPlan?.map((item, idx) => (
                        <li key={idx}>{item}</li>
                      ))}
                    </ul>
                  </div>

                  <div className="support-card">
                    <h2><Heart size={18} /> Reinforcement</h2>
                    <ul className="positive-list bubbly-list">
                      {recommendations?.positiveReinforcement?.map((item, idx) => (
                        <li key={idx} className="bubble-item">{item}</li>
                      ))}
                    </ul>
                  </div>
                </div>
              </motion.div>
            )}
          </AnimatePresence>
        </div>

        <AnimatePresence>
          {activeAction && (
            <RecommendationActionModal
              action={activeAction}
              onClose={() => setActiveAction(null)}
            />
          )}
        </AnimatePresence>
      </div>
    </div>
  );
}

export default Recommendations;
