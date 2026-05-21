import React, { useState, useEffect, useRef, useCallback } from 'react';
import { motion, AnimatePresence, useScroll } from 'framer-motion';
import { Smile, Sparkles, History, ChevronDown, BarChart2, CheckCircle2, Lightbulb, Map, User as UserIcon, Bell, Heart, ArrowRight } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import Sidebar from '../components/Sidebar';
import { Canvas } from '@react-three/fiber';
import { Environment } from '@react-three/drei';
import BreathingOrb from '../components/BreathingOrb';
import InteractiveMap from '../components/InteractiveMap';
import { fetchHabits, fetchTodayLogs } from '../controllers/HabitController';
import { fetchNotifications } from '../controllers/NotificationController.notification';
import API_BASE from '../utils/api.js';

// Custom SVG Progress Ring component
const ProgressRing = ({ radius, stroke, progress, color }) => {
  const normalizedRadius = radius - (stroke / 2);

  return (
    <div style={{ position: 'relative', width: radius * 2, height: radius * 2, display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
      <svg
        height={radius * 2}
        width={radius * 2}
        viewBox={`0 0 ${radius * 2} ${radius * 2}`}
        style={{ position: 'absolute', top: 0, left: 0, transform: 'rotate(-90deg)' }}
      >
        {/* Background track (the white circle) */}
        <circle
          stroke="white"
          fill="transparent"
          strokeWidth={stroke}
          r={normalizedRadius}
          cx={radius}
          cy={radius}
        />

        {/* Main progress ring (the purple circle) */}
        <motion.circle
          stroke={color}
          fill="transparent"
          strokeWidth={stroke}
          r={normalizedRadius}
          cx={radius}
          cy={radius}
          strokeLinecap="round"
          initial={{ pathLength: 0 }}
          animate={{ pathLength: progress / 100 }}
          transition={{ duration: 1.5, ease: "easeInOut" }}
        />
      </svg>
      <div style={{ textAlign: 'center', zIndex: 1 }}>
        <span style={{ fontSize: '2.5rem', fontWeight: '800', color: 'var(--text-main)', display: 'block', lineHeight: 1 }}>{Math.round(progress)}%</span>
      </div>
    </div>
  );
};



function Dashboard() {
  const [user, setUser] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [reflectionText, setReflectionText] = useState('');
  const [reflections, setReflections] = useState([]);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [showSuccess, setShowSuccess] = useState(false);
  const [isReflectionFocused, setIsReflectionFocused] = useState(false);


  const [hasLoggedMood, setHasLoggedMood] = useState(false);
  const [habits, setHabits] = useState([]);
  const [habitLogs, setHabitLogs] = useState([]);
  const [moodLogs, setMoodLogs] = useState([]);
  const [unreadCount, setUnreadCount] = useState(0);
  const [showAllLogs, setShowAllLogs] = useState(false);
  const navigate = useNavigate();


  const containerRef = useRef(null);
  const heroSectionRef = useRef(null);


  // Track scroll inside the content area
  const { scrollYProgress } = useScroll({ container: containerRef });

  useEffect(() => {
    const savedUser = localStorage.getItem('user');
    if (savedUser) {
      const parsedUser = JSON.parse(savedUser);
      setUser(parsedUser);
      fetchHabitsData(parsedUser.id);
      fetchTodayLogsData(parsedUser.id);
      fetchTodayMoodData(parsedUser.email);
      loadUnreadCount(parsedUser.id);
    }
    if (isModalOpen) fetchReflections();
  }, [isModalOpen]);

  const loadUnreadCount = async (userId) => {
    try {
      const data = await fetchNotifications(userId);
      const count = Array.isArray(data) ? data.filter(n => !n.read).length : 0;
      setUnreadCount(count);
    } catch (err) { /* silently ignore */ }
  };

  // Refresh unread count every 30 s to stay in sync with the notifications page
  useEffect(() => {
    if (!user?.id) return;
    const interval = setInterval(() => loadUnreadCount(user.id), 30000);
    return () => clearInterval(interval);
  }, [user?.id]);



  // ─────────────────────────────────────────────────────────────────────────

  const fetchHabitsData = async (userId) => {
    try {
      const data = await fetchHabits(userId);
      setHabits(data);
    } catch (err) { console.error(err); }
  };

  const fetchTodayLogsData = async (userId) => {
    try {
      const data = await fetchTodayLogs(userId);
      setHabitLogs(data);
    } catch (err) { console.error(err); }
  };

  const fetchTodayMoodData = async (userEmail) => {
    if (!userEmail) return;
    try {
      const response = await fetch(`${API_BASE}/api/moods?userId=${userEmail}`);
      if (!response.ok) return;
      const data = await response.json();
      const todayStr = new Date().toISOString().split('T')[0];
      const todayMoods = data.filter(m => m.date === todayStr);
      setMoodLogs(todayMoods);
    } catch (err) { console.error('Failed to fetch mood logs:', err); }
  };

  const getGreeting = () => {
    const hour = new Date().getHours();
    if (hour < 12) return 'Good morning';
    if (hour < 18) return 'Good afternoon';
    return 'Good evening';
  };

  const getDailyQuote = () => {
    const quotes = [
      { text: "The only way to do great work is to love what you do.", author: "Steve Jobs" },
      { text: "Believe you can and you're halfway there.", author: "Theodore Roosevelt" },
      { text: "The future depends on what you do today.", author: "Mahatma Gandhi" },
      { text: "It does not matter how slowly you go as long as you do not stop.", author: "Confucius" },
      { text: "Your time is limited, don't waste it living someone else's life.", author: "Steve Jobs" },
      { text: "Success is not final, failure is not fatal: it is the courage to continue that counts.", author: "Winston Churchill" },
      { text: "The only limit to our realization of tomorrow will be our doubts of today.", author: "Franklin D. Roosevelt" }
    ];
    // Use current date as index to pick a quote that changes daily
    const dayOfYear = Math.floor((new Date() - new Date(new Date().getFullYear(), 0, 0)) / (1000 * 60 * 60 * 24));
    return quotes[dayOfYear % quotes.length];
  };

  const fetchReflections = async () => {
    if (!user) return;

    try {
      const idToUse = user.id || user.email;
      const response = await fetch(`${API_BASE}/api/reflections?userId=${idToUse}`);

      if (response.ok) {
        const data = await response.json();
        setReflections(data);
      }
    } catch (err) { console.error(err); }
  };

  const handleSubmitReflection = async (e) => {
    e.preventDefault();
    if (!reflectionText.trim() || !user) return;
    setIsSubmitting(true);
    try {
      const idToUse = user.id || user.email;
      const response = await fetch(`${API_BASE}/api/reflections`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          text: reflectionText,
          userId: idToUse
        })
      });

      if (response.ok) {
        setReflectionText('');
        setShowSuccess(true);
        // Refresh local list immediately
        await fetchReflections();

        setTimeout(() => {
          setShowSuccess(false);
          setIsReflectionFocused(false);
        }, 3000);
      }


    } catch (err) { console.error("Failed to save reflection:", err); }
    finally { setIsSubmitting(false); }
  };

  const calculateDailyProgress = () => {
    const dailyHabits = habits.filter(h => h.periodicity === 'daily');
    if (dailyHabits.length === 0) return 0;

    // Sum up progress but cap each habit at its target count so extra completions don't exceed 100% total
    const totalCurrent = dailyHabits.reduce((acc, h) => acc + Math.min(h.currentCount || 0, h.targetCount || 1), 0);
    const totalTarget = dailyHabits.reduce((acc, h) => acc + (h.targetCount || 1), 0);

    return Math.min(100, Math.round((totalCurrent / totalTarget) * 100));
  };




  const calculateStreak = () => {
    if (habits.length === 0) return 0;
    // Find the highest persistent streak among all habits
    return Math.max(...habits.map(h => h.streak || 0));
  };


  return (
    <div className="dashboard">
      <div className="app-container">
        <Sidebar />

        {/* 3D BACKGROUND CANVAS */}
        <div style={{ position: 'fixed', top: 0, left: '280px', width: 'calc(100% - 280px)', height: '100vh', zIndex: 0, background: 'linear-gradient(to bottom, #F3F1FB, #EBE5F7)', pointerEvents: 'none' }}>
          <Canvas camera={{ position: [0, 0, 4.5], fov: 45 }}>
            <ambientLight intensity={0.6} />
            <directionalLight position={[10, 10, 5]} intensity={1.5} color="#fff" />
            <directionalLight position={[-10, -10, -5]} intensity={0.5} color="#D6C8F5" />
            <Environment preset="city" />
            <BreathingOrb />
          </Canvas>
        </div>

        {/* SCROLLABLE HTML OVERLAY */}
        <div ref={containerRef} className="content-area scroll-snap-container no-scrollbar" style={{ zIndex: 10, padding: 0 }}>

          {/* SCENE 0: GREETING HEADER */}
          <motion.div
            initial={{ opacity: 0, y: -20 }}
            animate={{ opacity: 1, y: 0 }}
            onClick={() => navigate('/profile')}
            style={{
              position: 'absolute', top: '4vh', left: '4vw', zIndex: 100,
              display: 'flex', alignItems: 'center', gap: '1.5rem',
              cursor: 'pointer'
            }}
          >
            <div style={{
              width: '64px', height: '64px', borderRadius: '50%', background: 'var(--color-primary-light)',
              overflow: 'hidden', display: 'flex', justifyContent: 'center', alignItems: 'center',
              boxShadow: '0 10px 20px rgba(138, 100, 214, 0.15)', border: '3px solid white'
            }}>
              {user?.profilePicture ? (
                <img src={user.profilePicture} alt="Profile" style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
              ) : (
                <UserIcon size={32} color="var(--color-primary)" />
              )}
            </div>
            <div>
              <p style={{ margin: 0, fontSize: '1.1rem', fontWeight: 600, color: 'var(--color-primary)', textTransform: 'uppercase', letterSpacing: '0.1em' }}>
                {getGreeting()}
              </p>
              <h2 style={{ margin: 0, fontSize: '2.5rem', fontWeight: 800, color: 'var(--text-main)' }}>
                Hello, {user?.fullName?.split(' ')[0] || 'Friend'}.
              </h2>
            </div>
          </motion.div>

          {/* NOTIFICATION WIDGET — top-right, opposite the greeting */}
          <motion.div
            initial={{ opacity: 0, y: -20 }}
            animate={{ opacity: 1, y: 0 }}
            onClick={() => navigate('/notifications')}
            title={unreadCount > 0 ? `${unreadCount} unread notification${unreadCount === 1 ? '' : 's'}` : 'No new notifications'}
            style={{
              position: 'absolute', top: '4vh', right: '4vw', zIndex: 100,
              display: 'flex', alignItems: 'center', gap: '0.75rem',
              cursor: 'pointer',
              background: 'rgba(255,255,255,0.55)',
              backdropFilter: 'blur(12px)',
              border: '1.5px solid rgba(255,255,255,0.7)',
              borderRadius: '2rem',
              padding: '0.65rem 1.25rem',
              boxShadow: '0 8px 24px rgba(138,100,214,0.12)',
              transition: 'box-shadow 0.2s ease',
            }}
            whileHover={{ boxShadow: '0 12px 32px rgba(138,100,214,0.22)', scale: 1.03 }}
            whileTap={{ scale: 0.97 }}
          >
            <div style={{ position: 'relative' }}>
              <Bell size={22} color="var(--color-primary)" />
              {unreadCount > 0 && (
                <span style={{
                  position: 'absolute', top: '-6px', right: '-8px',
                  background: 'var(--color-primary)',
                  color: 'white',
                  borderRadius: '50%',
                  width: '18px', height: '18px',
                  fontSize: '0.7rem', fontWeight: 700,
                  display: 'flex', alignItems: 'center', justifyContent: 'center',
                  boxShadow: '0 2px 6px rgba(138,100,214,0.4)',
                  lineHeight: 1,
                }}>
                  {unreadCount > 9 ? '9+' : unreadCount}
                </span>
              )}
            </div>
            <span style={{
              fontSize: '0.95rem', fontWeight: 600,
              color: 'var(--text-main)',
              whiteSpace: 'nowrap',
            }}>
              {unreadCount === 0
                ? 'All caught up'
                : `${unreadCount} notification${unreadCount === 1 ? '' : 's'}`}
            </span>
          </motion.div>

          {/* SCENE 1: HERO */}
          <motion.section ref={heroSectionRef} className="scroll-snap-section" style={{ height: '100vh', display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center' }}>
            <h1 className="hero-text" style={{ fontSize: '5rem', marginBottom: '1rem', color: '#1F2937' }}>Take a moment.</h1>
            <p className="hero-subtext" style={{ fontSize: '1.5rem', marginBottom: '3rem' }}>Breathe in time with the orb.</p>

            <AnimatePresence>
              {!hasLoggedMood && (
                <motion.button
                  initial={{ opacity: 0, scale: 0.9 }} animate={{ opacity: 1, scale: 1 }} exit={{ opacity: 0, scale: 0.9 }}
                  className="glass-button animate-pulse-glow"
                  onClick={() => {
                    setHasLoggedMood(true);
                    // Scroll to the next snap section (Sanctuary Map)
                    setTimeout(() => {
                      if (heroSectionRef.current) {
                        heroSectionRef.current.nextElementSibling?.scrollIntoView({ behavior: 'smooth' });
                      }
                    }, 400);
                  }}
                  style={{ backgroundColor: 'var(--color-primary)', color: 'white', border: 'none', padding: '1.25rem 3rem', fontSize: '1.25rem' }}
                >
                  <Smile size={24} style={{ marginRight: '1rem' }} /> I'm here
                </motion.button>
              )}
            </AnimatePresence>

            <motion.div
              animate={{ y: [0, 10, 0] }} transition={{ repeat: Infinity, duration: 2, ease: "easeInOut" }}
              style={{ position: 'absolute', bottom: '5vh', color: 'var(--text-muted)' }}
            >
              <ChevronDown size={32} opacity={0.5} />
            </motion.div>
          </motion.section>

          {/* SCENE 2: INTERACTIVE SANCTUARY MAP */}
          <motion.section className="scroll-snap-section" style={{ height: '100vh', display: 'flex', flexDirection: 'column', justifyContent: 'flex-start', paddingTop: '8vh', alignItems: 'center', position: 'relative' }}>
            <h2 style={{ fontSize: '2.5rem', marginBottom: '0.5rem', fontWeight: 700, zIndex: 5 }}>Sanctuary Map</h2>
            <p style={{ color: 'var(--text-muted)', marginBottom: '2rem', zIndex: 5 }}>Navigate your wellness journey</p>

            <InteractiveMap />
          </motion.section>


          {/* SCENE 3: DAILY PULSE (PREVIEWS) */}
          <motion.section className="scroll-snap-section" style={{ height: '100vh', display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center' }}>
            <div style={{ display: 'flex', gap: '3rem', maxWidth: '1100px', width: '100%' }}>
              {/* Daily Quote Panel */}
              <div className="glass-panel" style={{ flex: 1, padding: '3rem', display: 'flex', flexDirection: 'column', justifyContent: 'space-between', background: 'rgba(255, 255, 255, 0.4)' }}>
                <div>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', marginBottom: '2rem' }}>
                    <div style={{ padding: '1rem', background: 'var(--color-primary-light)', borderRadius: '50%', color: 'var(--color-primary)' }}>
                      <Lightbulb size={32} />
                    </div>
                    <h2 style={{ margin: 0 }}>Daily Wisdom</h2>
                  </div>
                  <div style={{ position: 'relative' }}>
                    <span style={{ position: 'absolute', top: '-1.5rem', left: '-1rem', fontSize: '4rem', opacity: 0.1, color: 'var(--color-primary)', fontFamily: 'serif' }}>“</span>
                    <p style={{ fontSize: '1.6rem', color: 'var(--text-main)', fontWeight: '600', fontStyle: 'italic', lineHeight: 1.4, marginBottom: '1.5rem' }}>
                      {getDailyQuote().text}
                    </p>
                    <p style={{ fontSize: '1.1rem', color: 'var(--text-muted)', fontWeight: '700', textAlign: 'right' }}>
                      — {getDailyQuote().author}
                    </p>
                  </div>
                </div>
                <div style={{ marginTop: '2rem', height: '4px', width: '40px', background: 'var(--color-primary)', borderRadius: '2px', opacity: 0.3 }}></div>
              </div>

              {/* Recommendation Preview */}
              <DailySparkPanel userId={user?.id} onSeeMore={() => navigate('/recommendations')} />
            </div>
          </motion.section>

          <motion.section className="scroll-snap-section" style={{ height: '100vh', display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center' }}>
            <h2 style={{ fontSize: '3rem', marginBottom: '4rem', fontWeight: 700 }}>Your progress flows.</h2>
            <div className="glass-panel" style={{ padding: '4rem', display: 'flex', gap: '5rem', alignItems: 'center' }}>
              <div style={{ textAlign: 'center', display: 'flex', flexDirection: 'column', alignItems: 'center', flex: 1 }}>
                <p style={{ color: 'var(--text-muted)', marginBottom: '2rem', fontSize: '1.25rem', fontWeight: '700', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Daily Habits</p>
                <ProgressRing radius={100} stroke={14} progress={calculateDailyProgress()} color="var(--color-primary)" />
                <p style={{ marginTop: '2.5rem', fontWeight: '700', fontSize: '1.2rem', color: 'var(--text-main)' }}>
                  {habits.filter(h => h.periodicity === 'daily').reduce((acc, h) => acc + Math.min(h.currentCount || 0, h.targetCount || 1), 0)} / {habits.filter(h => h.periodicity === 'daily').reduce((acc, h) => acc + (h.targetCount || 1), 0)} tasks completed
                </p>

              </div>

              <div style={{ width: '1px', height: '240px', background: 'rgba(0,0,0,0.1)' }}></div>

              <div style={{ textAlign: 'center', display: 'flex', flexDirection: 'column', alignItems: 'center', flex: 1 }}>
                <p style={{ color: 'var(--text-muted)', marginBottom: '2rem', fontSize: '1.25rem', fontWeight: '700', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Current Streak</p>
                <div style={{
                  width: '200px',
                  height: '200px',
                  borderRadius: '50%',
                  background: 'var(--color-primary-light)',
                  display: 'flex',
                  justifyContent: 'center',
                  alignItems: 'center',
                  boxShadow: '0 15px 35px rgba(138, 100, 214, 0.25)',
                  border: '4px solid white'
                }}>
                  <Sparkles color="var(--color-primary)" size={80} />
                </div>
                <p style={{ marginTop: '2.5rem', fontWeight: '700', fontSize: '1.25rem', color: 'var(--color-primary)' }}>{calculateStreak()} Days Strong</p>
              </div>

            </div>
          </motion.section>

          {/* SCENE 5: TIMELINE PATH */}
          <motion.section className="scroll-snap-section" style={{ height: '100vh', display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', paddingTop: '40vh' }}>
            <h2 style={{ fontSize: '3rem', marginBottom: '3rem', fontWeight: 700 }}>The path you walked today.</h2>

            {(() => {
              // Merge habit logs and mood logs into one sorted timeline
              const habitEntries = habitLogs.map(log => ({
                key: 'habit-' + log.id,
                time: new Date(log.timestamp),
                label: `Completed "${log.habitName}"`,
                sub: new Date(log.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
                color: 'var(--color-primary)'
              }));
              const moodEntries = moodLogs.map((m, i) => ({
                key: 'mood-' + i,
                time: new Date(m.date + 'T12:00:00'), // mood has date only, treat as noon
                label: `Logged mood: ${m.mood?.charAt(0).toUpperCase() + m.mood?.slice(1)}`,
                sub: 'Today',
                color: '#A78BFA'
              }));
              const allEntries = [...habitEntries, ...moodEntries].sort((a, b) => b.time - a.time);

              if (allEntries.length === 0) {
                return (
                  <div className="glass-panel" style={{ padding: '4rem 6rem', maxWidth: '600px', width: '100%', textAlign: 'center', color: 'var(--text-muted)' }}>
                    <p>No activity logged yet today.</p>
                  </div>
                );
              }

              const visible = showAllLogs ? allEntries : allEntries.slice(0, 3);
              return (
                <div className="glass-panel" style={{ padding: '4rem 6rem', maxWidth: '600px', width: '100%', display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
                  {visible.map(entry => (
                    <div key={entry.key} className="timeline-item" style={{ borderBottom: '1px solid rgba(0,0,0,0.05)', paddingBottom: '1rem' }}>
                      <p style={{ fontWeight: '600', margin: 0, fontSize: '1.25rem', color: entry.color }}>{entry.label}</p>
                      <p style={{ fontSize: '1rem', color: 'var(--text-muted)', margin: 0 }}>{entry.sub}</p>
                    </div>
                  ))}
                  {allEntries.length > 3 && (
                    <button
                      onClick={() => setShowAllLogs(!showAllLogs)}
                      style={{
                        marginTop: '1rem', alignSelf: 'center', background: 'none', border: 'none',
                        color: 'var(--color-primary)', fontWeight: '700', cursor: 'pointer',
                        fontSize: '1rem', display: 'flex', alignItems: 'center', gap: '0.5rem'
                      }}
                    >
                      {showAllLogs ? 'Show Less' : `Show More (${allEntries.length - 3} more)`}
                      <ChevronDown style={{ transform: showAllLogs ? 'rotate(180deg)' : 'none', transition: 'transform 0.3s ease' }} size={18} />
                    </button>
                  )}
                </div>
              );
            })()}

          </motion.section>

          {/* SCENE 6: REFLECTION */}
          <motion.section className="scroll-snap-section" style={{ height: '100vh', display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', paddingTop: '40vh' }}>

            <div className="glass-panel" style={{ padding: '4rem', maxWidth: '800px', width: '100%', display: 'flex', flexDirection: 'column' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '3rem' }}>
                <h2 style={{ fontSize: '2.5rem', margin: 0 }}>Release your thoughts.</h2>
                <button onClick={() => setIsModalOpen(true)} style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', color: 'var(--text-muted)', fontWeight: 'bold', background: 'rgba(255,255,255,0.5)', padding: '0.75rem 1.5rem', borderRadius: 'var(--radius-md)' }}>
                  <History size={20} /> Past entries
                </button>
              </div>

              <AnimatePresence>
                {isReflectionFocused && (
                  <motion.div initial={{ opacity: 0, height: 0 }} animate={{ opacity: 1, height: 'auto' }} exit={{ opacity: 0, height: 0 }} style={{ overflow: 'hidden', marginBottom: '1.5rem' }}>
                    <p style={{ fontWeight: '600', fontSize: '1.25rem', color: 'var(--color-primary)' }}>💡 What made today better? Anything stressing you?</p>
                  </motion.div>
                )}
              </AnimatePresence>

              <form onSubmit={handleSubmitReflection} style={{ display: 'flex', flexDirection: 'column', flex: 1 }}>
                <motion.textarea
                  layout
                  value={reflectionText}
                  onChange={(e) => setReflectionText(e.target.value)}
                  onFocus={() => setIsReflectionFocused(true)}
                  onBlur={() => !reflectionText && setIsReflectionFocused(false)}
                  placeholder="How are you feeling right now?"
                  style={{
                    flex: 1, width: '100%', padding: '2rem', borderRadius: 'var(--radius-lg)',
                    border: isReflectionFocused ? '2px solid var(--color-primary)' : '2px solid rgba(255,255,255,0.8)',
                    marginBottom: '2rem', minHeight: isReflectionFocused ? '250px' : '150px',
                    resize: 'none', fontFamily: 'inherit', fontSize: '1.25rem',
                    background: isReflectionFocused ? 'rgba(255,255,255,0.95)' : 'rgba(255,255,255,0.6)',
                    transition: 'all 0.3s ease', outline: 'none',
                    boxShadow: isReflectionFocused ? '0 15px 35px rgba(0,0,0,0.1)' : 'none'
                  }}
                />
                <AnimatePresence>
                  {(isReflectionFocused || reflectionText || showSuccess) && (
                    <motion.button
                      initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} exit={{ opacity: 0, y: 10 }}
                      type="submit" className="glass-button" disabled={isSubmitting || showSuccess}
                      style={{ width: '100%', backgroundColor: showSuccess ? '#d3d3d3' : 'var(--color-primary)', color: showSuccess ? '#555' : 'white', padding: '1.5rem', fontSize: '1.25rem' }}
                    >
                      {isSubmitting ? '...' : showSuccess ? 'Reflection Logged!' : 'Save Reflection'}
                    </motion.button>
                  )}
                </AnimatePresence>
              </form>
            </div>
          </motion.section>

          {/* Buffer section for scrolling past reflection smoothly */}
          <section style={{ height: '50vh' }}></section>

        </div>
      </div>

      {/* MODAL OVERLAY: PAST REFLECTIONS */}
      <AnimatePresence>
        {isModalOpen && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            className="modal-overlay"
            onClick={() => setIsModalOpen(false)}
          >
            <motion.div
              initial={{ scale: 0.9, y: 20 }}
              animate={{ scale: 1, y: 0 }}
              exit={{ scale: 0.9, y: 20 }}
              className="modal-content glass-panel"
              onClick={(e) => e.stopPropagation()}
              style={{ maxWidth: '600px', width: '90%', maxHeight: '80vh', overflowY: 'auto', padding: '3rem' }}
            >
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
                <h2 style={{ margin: 0 }}>Your Reflections</h2>
                <button onClick={() => setIsModalOpen(false)} style={{ fontSize: '1.5rem', color: 'var(--text-muted)' }}>&times;</button>
              </div>

              {reflections.length === 0 ? (
                <p style={{ textAlign: 'center', padding: '2rem' }}>No entries yet. Start writing today!</p>
              ) : (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
                  {reflections.map((ref) => (
                    <div key={ref.id} style={{ padding: '1.5rem', background: 'rgba(255,255,255,0.5)', borderRadius: 'var(--radius-md)', border: '1px solid rgba(255,255,255,0.3)' }}>
                      <p style={{ margin: '0 0 0.5rem 0', color: 'var(--text-main)', fontSize: '1.1rem', lineHeight: '1.6' }}>{ref.text}</p>
                      <p style={{ margin: 0, fontSize: '0.85rem', color: 'var(--text-muted)' }}>
                        {new Date(ref.timestamp).toLocaleDateString()} at {new Date(ref.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                      </p>
                    </div>
                  ))}
                </div>
              )}
            </motion.div>
          </motion.div>
        )}
      </AnimatePresence>
    </div>



  );
}


const DailySparkPanel = ({ userId, onSeeMore }) => {
  const [microAction, setMicroAction] = useState(null);

  useEffect(() => {
    if (!userId) return;
    fetch(`${API_BASE}/api/recommendations?mood=neutral&userId=${userId}`)
      .then(r => r.ok ? r.json() : null)
      .then(data => {
        if (data?.microActions?.length > 0) {
          setMicroAction(data.microActions[0]);
        }
      })
      .catch(() => { });
  }, [userId]);

  const displayText = microAction
    ? `"${microAction.title} — ${microAction.description}"`
    : '"Take 3 deep breaths and reset your focus"';

  return (
    <div className="glass-panel" style={{ flex: 1, padding: '3rem', display: 'flex', flexDirection: 'column', justifyContent: 'space-between', borderLeft: '4px solid var(--color-yellow)' }}>
      <div>
        <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', marginBottom: '2rem' }}>
          <div style={{ padding: '1rem', background: 'var(--color-yellow)', borderRadius: '50%', color: '#B8860B' }}>
            <Sparkles size={32} />
          </div>
          <h2 style={{ margin: 0 }}>Daily Spark</h2>
        </div>
        <p style={{ fontSize: '1.4rem', color: 'var(--text-main)', fontWeight: '500', lineHeight: 1.5 }}>
          {displayText}
        </p>
      </div>
      <motion.button
        whileHover={{ scale: 1.04 }}
        whileTap={{ scale: 0.97 }}
        className="glass-button"
        onClick={onSeeMore}
        style={{ marginTop: '2rem', width: 'fit-content', backgroundColor: 'var(--color-primary)', color: 'white', border: 'none' }}
      >
        <Sparkles size={16} style={{ marginRight: '0.5rem' }} />
        See more
      </motion.button>
    </div>
  );
};

export default Dashboard;
