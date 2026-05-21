import React, { useState, useEffect, useRef } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { X, Play, Pause, RotateCcw, Heart, Send, Wind, MessageCircle, Sparkles, Sun, TreePine, Timer, Smile, Ear, ChevronRight, Phone, CircleDot, Volume2, CloudRain, Waves } from 'lucide-react';

const RecommendationActionModal = ({ action, onClose }) => {
  if (!action) return null;

  return (
    <motion.div 
      initial={{ opacity: 0 }} 
      animate={{ opacity: 1 }} 
      exit={{ opacity: 0 }}
      className="premium-modal-overlay"
    >
      <motion.div 
        initial={{ scale: 0.95, opacity: 0, y: 30 }}
        animate={{ scale: 1, opacity: 1, y: 0 }}
        exit={{ scale: 0.95, opacity: 0, y: 30 }}
        className="premium-action-card"
      >
        <button className="premium-close-btn" onClick={onClose}><X size={24} /></button>
        
        <div className="action-container">
          {action.type === 'timer' && <AdvancedTimerView duration={action.duration} title={action.title} />}
          {action.type === 'reflection' && <AdvancedReflectionView onClose={onClose} />}
          {action.type === 'message-love' && <MessageLoveView onClose={onClose} />}
          {action.type === 'garden-stopwatch' && <GardenStopwatchView onClose={onClose} />}
          {action.type === 'breathing' && <AdvancedBreathingView />}
          {action.type === 'relaxation' && <RelaxationGuideView onClose={onClose} />}
          {action.type === 'pressure' && <PressureReleaseView onClose={onClose} />}
          {action.type === 'nature-sounds' && <NatureSoundsView onClose={onClose} />}
        </div>
      </motion.div>
    </motion.div>
  );
};

const NatureSoundsView = ({ onClose }) => {
  const [selectedSound, setSelectedSound] = useState(null);
  const [isPlaying, setIsPlaying] = useState(false);
  const audioRef = useRef(null);

  const sounds = [
    {
      id: 'ocean',
      title: 'Ocean Waves',
      icon: <Waves size={24} />,
      url: '/audio/oceanwaves.mp3',
      visual: 'ocean-waves'
    },
    {
      id: 'rain',
      title: 'Rain on Windows',
      icon: <CloudRain size={24} />,
      url: '/audio/rainonwindow.mp3',
      visual: 'rain-glass'
    },
    {
      id: 'wind',
      title: 'Wind through Trees',
      icon: <Wind size={24} />,
      url: '/audio/windthroughtrees.mp3',
      visual: 'wind-forest'
    }
  ];

  const handlePlay = (sound) => {
    if (audioRef.current) {
      audioRef.current.pause();
    }
    setSelectedSound(sound);
    setIsPlaying(true);
    audioRef.current = new Audio(sound.url);
    audioRef.current.loop = true;
    audioRef.current.play();
  };

  const handleStop = () => {
    if (audioRef.current) {
      audioRef.current.pause();
    }
    setIsPlaying(false);
    setSelectedSound(null);
  };

  useEffect(() => {
    return () => {
      if (audioRef.current) {
        audioRef.current.pause();
      }
    };
  }, []);

  return (
    <div className="premium-view nature-view">
      <h2 className="premium-title">Immersive Nature</h2>
      
      {!selectedSound ? (
        <div className="sound-selection-grid">
          {sounds.map((s, i) => (
            <motion.button 
              key={s.id} 
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: i * 0.1 }}
              whileHover={{ y: -10, scale: 1.02 }}
              className={`sound-card-btn ${s.id}-card`} 
              onClick={() => handlePlay(s)}
            >
              <motion.div 
                animate={{ y: [0, -5, 0] }}
                transition={{ repeat: Infinity, duration: 2 + i, ease: "easeInOut" }}
                className="sound-icon-circle"
              >
                {s.icon}
              </motion.div>
              <span className="sound-title-label">{s.title}</span>
            </motion.button>
          ))}
        </div>
      ) : (
        <motion.div 
          initial={{ opacity: 0, scale: 0.9 }} 
          animate={{ opacity: 1, scale: 1 }} 
          className="active-sound-stage"
        >
          <div className={`sound-visual-display ${selectedSound.id}`}>
             <div className="visual-overlay"></div>
             {selectedSound.id === 'ocean' && <OceanVisual />}
             {selectedSound.id === 'rain' && <RainVisual />}
             {selectedSound.id === 'wind' && <WindVisual />}
          </div>
          <motion.h3 
            animate={{ opacity: [0.5, 1, 0.5] }}
            transition={{ repeat: Infinity, duration: 3 }}
            className="playing-status"
          >
            Listening to {selectedSound.title}...
          </motion.h3>
          <div className="premium-controls">
            <button className="p-btn primary stop-btn" onClick={handleStop}>
              <Pause size={20} /> Stop Sound
            </button>
          </div>

        </motion.div>
      )}
      
      <p className="nature-desc">Nature sounds help lower cortisol and improve concentration.</p>
    </div>
  );
};

/* Nature Visual Components */
const OceanVisual = () => (
  <div className="v-ocean">
    <motion.div animate={{ y: [0, -20, 0] }} transition={{ repeat: Infinity, duration: 4 }} className="wave-layer" />
    <motion.div animate={{ y: [0, -30, 0] }} transition={{ repeat: Infinity, duration: 5, delay: 1 }} className="wave-layer opac" />
  </div>
);

const RainVisual = () => (
  <div className="v-rain">
    {[...Array(12)].map((_, i) => (
      <motion.div 
        key={i}
        animate={{ y: [0, 150], opacity: [0, 1, 0] }}
        transition={{ repeat: Infinity, duration: 0.8 + Math.random(), delay: Math.random() }}
        className="rain-drop"
        style={{ left: `${i * 10}%` }}
      />
    ))}
  </div>
);

const WindVisual = () => (
  <div className="v-wind">
    <motion.div animate={{ rotate: [0, 5, 0] }} transition={{ repeat: Infinity, duration: 3 }} className="tree-sway-large">
      <TreePine size={80} color="#065f46" />
    </motion.div>
    <div className="leaf-particles">
      {[...Array(6)].map((_, i) => (
        <motion.div 
          key={i}
          animate={{ x: [-20, 200], y: [-10, 50], rotate: 360 }}
          transition={{ repeat: Infinity, duration: 2 + Math.random(), delay: Math.random() }}
          className="leaf"
        />
      ))}
    </div>
  </div>
);


const PressureReleaseView = ({ onClose }) => {
  const [step, setStep] = useState(0);
  const steps = [
    {
      title: "Valsalva Maneuver",
      desc: "Gently pinch your nose shut, close your mouth, and try to blow out gently through your nose.",
      visual: "nose-blow",
    },
    {
      title: "Toynbee Maneuver",
      desc: "Swallow while pinching your nose shut. This activates muscles that open the tubes.",
      visual: "pinch-swallow",
    },
    {
      title: "Swallowing & Yawning",
      desc: "These motions flex the muscles in your throat and encourage the Eustachian tubes to open.",
      visual: "yawn-flex",
    },
    {
      title: "Chewing & Sucking",
      desc: "Chewing gum or sucking on hard candy promotes saliva production and swallowing.",
      visual: "chew-saliva",
    }
  ];

  const handleNotWorked = () => {
    if (step < steps.length - 1) {
      setStep(step + 1);
    } else {
      setStep(99); 
    }
  };

  return (
    <div className="premium-view pressure-view">
      <AnimatePresence mode="wait">
        {step < steps.length ? (
          <motion.div 
            key={step}
            initial={{ opacity: 0, x: 20 }}
            animate={{ opacity: 1, x: 0 }}
            exit={{ opacity: 0, x: -20 }}
            className="pressure-step-container"
          >
            <div className="pressure-icon-badge cute-badge">
              <motion.div
                animate={{ scale: [1, 1.1, 1] }}
                transition={{ repeat: Infinity, duration: 2 }}
              >
                <Ear size={48} color="var(--color-primary)" />
              </motion.div>
            </div>

            <h2 className="premium-title">{steps[step].title}</h2>
            <p className="p-desc-large">{steps[step].desc}</p>
            
            <div className="pressure-visual-stage">
              {steps[step].visual === 'nose-blow' && <NoseBlowVisual />}
              {steps[step].visual === 'pinch-swallow' && <PinchSwallowVisual />}
              {steps[step].visual === 'yawn-flex' && <YawnFlexVisual />}
              {steps[step].visual === 'chew-saliva' && <ChewSalivaVisual />}
            </div>
            
            <div className="step-actions-p side-by-side">
              <button className="p-btn primary" onClick={onClose}>
                It Worked!
              </button>
              <button className="p-btn secondary" onClick={handleNotWorked}>
                Not Yet <ChevronRight size={18} />
              </button>
            </div>

          </motion.div>
        ) : step === 99 ? (
          <motion.div initial={{ opacity: 0, scale: 0.9 }} animate={{ opacity: 1, scale: 1 }} className="pressure-fail centered-content">
            <div className="fail-icon-box">
              <Phone size={48} color="#EF4444" />
            </div>
            <h2 className="premium-title">Still Feeling Pressure?</h2>
            <p className="p-message-fail">
              Sometimes persistent pressure needs extra support. 
              <strong> Call someone you trust</strong> to let them know how you're feeling.
            </p>
            <div className="center-btn-wrap">
              <button className="p-btn primary large" onClick={onClose}>Close Guide</button>
            </div>
          </motion.div>

        ) : null}
      </AnimatePresence>
    </div>
  );
};

/* Specific Visual Components for Pressure Release */

const NoseBlowVisual = () => (
  <div className="instructional-visual valsalva">
    <div className="head-silhouette">
      <motion.div 
        animate={{ scale: [1, 1.05, 1] }}
        transition={{ repeat: Infinity, duration: 2 }}
        className="nose-pinch-point"
      />
      <div className="air-path-arrows">
        <motion.div animate={{ x: [0, 50], opacity: [0, 1, 0] }} transition={{ repeat: Infinity, duration: 1.5 }} className="air-line" />
        <motion.div animate={{ x: [0, 50], opacity: [0, 1, 0], y: 15 }} transition={{ repeat: Infinity, duration: 1.5, delay: 0.2 }} className="air-line" />
      </div>
    </div>
    <div className="instruction-text-overlay">Pinch Nose & Blow Gently</div>
  </div>
);



const PinchSwallowVisual = () => (
  <div className="instructional-visual toynbee">
    <div className="throat-silhouette">
      <motion.div 
        animate={{ y: [0, -25, 0] }}
        transition={{ repeat: Infinity, duration: 2, ease: "easeInOut" }}
        className="swallow-indicator-bar"
      />
      <div className="pinch-marker" />
    </div>
    <div className="instruction-text-overlay">Pinch Nose & Swallow</div>
  </div>
);



const YawnFlexVisual = () => (
  <div className="instructional-visual yawn">
    <div className="mouth-silhouette">
      <motion.div 
        animate={{ height: [20, 70, 20], opacity: [0.3, 0.8, 0.3] }}
        transition={{ repeat: Infinity, duration: 3 }}
        className="mouth-opening-bar"
      />
      <div className="muscle-tension-lines">
        <motion.div animate={{ opacity: [0.1, 1, 0.1] }} transition={{ repeat: Infinity, duration: 3 }} className="t-line" />
      </div>
    </div>
    <div className="instruction-text-overlay">Wide Yawning Motion</div>
  </div>
);



const ChewSalivaVisual = () => (
  <div className="instructional-visual chew">
    <div className="jaw-silhouette">
      <motion.div 
        animate={{ y: [0, 15, 0] }}
        transition={{ repeat: Infinity, duration: 0.8 }}
        className="lower-jaw-bar"
      />
      <div className="saliva-pulse">
        <motion.div animate={{ scale: [1, 1.3, 1], opacity: [0.2, 0.4, 0.2] }} transition={{ repeat: Infinity, duration: 1.2 }} className="s-pulse" />
      </div>
    </div>
    <div className="instruction-text-overlay">Chew & Swallow Rhythmicly</div>
  </div>
);



const AdvancedTimerView = ({ duration, title }) => {
  const [timeLeft, setTimeLeft] = useState(duration * 60);
  const [isActive, setIsActive] = useState(false);
  const totalSeconds = duration * 60;
  const percentage = (timeLeft / totalSeconds) * 100;

  useEffect(() => {
    let interval = null;
    if (isActive && timeLeft > 0) {
      interval = setInterval(() => setTimeLeft((prev) => prev - 1), 1000);
    } else if (timeLeft === 0) {
      setIsActive(false);
      clearInterval(interval);
    }
    return () => clearInterval(interval);
  }, [isActive, timeLeft]);

  const formatTime = (seconds) => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins}:${secs < 10 ? '0' : ''}${secs}`;
  };

  return (
    <div className="premium-view timer-view">
      <div className="timer-header">
        <Sparkles size={20} className="accent-icon" />
        <span className="subtitle">Mello Flow</span>
      </div>
      <h2 className="premium-title">{title}</h2>
      
      <div className="progress-ring-wrapper">
        <svg className="progress-ring" width="260" height="260" viewBox="0 0 260 260">
          <circle 
            className="progress-ring-bg" 
            stroke="#f1f5f9" 
            strokeWidth="12" 
            fill="transparent" 
            r="110" 
            cx="130" 
            cy="130"
          />

          <motion.circle 
            className="progress-ring-bar" 
            stroke="var(--color-primary)" 
            strokeWidth="12" 
            strokeLinecap="round"
            fill="transparent" 
            r="110" 
            cx="130" 
            cy="130"
            initial={{ pathLength: 0 }}
            animate={{ pathLength: percentage / 100 }}
            style={{ 
              rotate: -90,
              originX: "50%",
              originY: "50%",
              filter: 'drop-shadow(0px 0px 8px rgba(138, 100, 214, 0.4))'
            }}
          />
        </svg>

        <div className="timer-display">
          <motion.span 
            key={timeLeft}
            initial={{ scale: 1.1, opacity: 0.8 }}
            animate={{ scale: 1, opacity: 1 }}
            className="time-val"
          >
            {formatTime(timeLeft)}
          </motion.span>
          <span className="time-label">{isActive ? 'FLOWING' : 'READY'}</span>
        </div>
      </div>

      <div className="premium-controls">
        <button className={`p-btn primary ${isActive ? 'active' : ''}`} onClick={() => setIsActive(!isActive)}>
          {isActive ? <Pause size={24} /> : <Play size={24} />}
          <span>{isActive ? 'Pause' : 'Start Session'}</span>
        </button>
        <button className="p-btn secondary" onClick={() => { setTimeLeft(totalSeconds); setIsActive(false); }}>
          <RotateCcw size={20} />
        </button>
      </div>
    </div>
  );
};

const RelaxationGuideView = ({ onClose }) => {
  const [step, setStep] = useState(0);
  const steps = [
    { text: "Find a comfortable seat. Close your eyes if you can.", anim: "center" },
    { text: "Lift your shoulders high toward your ears. Tighten them...", anim: "lift" },
    { text: "Now... DROP them. Let the weight fall away.", anim: "drop" },
    { text: "Part your lips slightly. Relax your jaw completely.", anim: "jaw" },
    { text: "Feel the tension leaving your body with every breath.", anim: "final" }
  ];

  useEffect(() => {
    if (step < steps.length - 1) {
      const timer = setTimeout(() => setStep(step + 1), 4000);
      return () => clearTimeout(timer);
    }
  }, [step]);

  return (
    <div className="premium-view relaxation-guide">
      <div className="guide-visual-container">
        <div className="instructional-visual relaxation">
          <div className="torso-silhouette">
            <motion.div 
              animate={steps[step].anim === 'lift' ? { y: -25 } : steps[step].anim === 'drop' ? { y: 10 } : { y: 0 }}
              transition={{ duration: 1.5, ease: "easeInOut" }}
              className="shoulder-indicator-line left"
            />
            <motion.div 
              animate={steps[step].anim === 'lift' ? { y: -25 } : steps[step].anim === 'drop' ? { y: 10 } : { y: 0 }}
              transition={{ duration: 1.5, ease: "easeInOut" }}
              className="shoulder-indicator-line right"
            />
            {steps[step].anim === 'jaw' && (
              <motion.div 
                animate={{ height: [10, 30, 10] }}
                transition={{ repeat: Infinity, duration: 2 }}
                className="mouth-opening-indicator"
              />
            )}
          </div>
          <div className="instruction-text-overlay">{steps[step].text.split('.')[0]}</div>
        </div>
        
        <div className="particles">
          <motion.div animate={{ y: [-10, -100], opacity: [0, 1, 0] }} transition={{ repeat: Infinity, duration: 3 }} className="particle" />
          <motion.div animate={{ y: [-10, -80], opacity: [0, 1, 0] }} transition={{ repeat: Infinity, duration: 2.5, delay: 0.5 }} className="particle" />
        </div>
      </div>

      <div className="guide-content">
        <motion.p 
          key={step}
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          className="guide-step-text"
        >
          {steps[step].text}
        </motion.p>
        <div className="guide-progress">
          {steps.map((_, i) => (
            <div key={i} className={`progress-dot ${i <= step ? 'active' : ''}`} />
          ))}
        </div>
      </div>

      {step === steps.length - 1 && (
        <motion.button 
          initial={{ opacity: 0 }} animate={{ opacity: 1 }}
          className="p-btn primary" onClick={onClose}
        >
          I feel better
        </motion.button>
      )}
    </div>
  );
};

const MessageLoveView = ({ onClose }) => (
  <div className="premium-view social-view">
    <div className="p-icon-header">
      <motion.div 
        animate={{ 
          scale: [1, 1.15, 1],
          rotate: [0, 5, -5, 0]
        }} 
        transition={{ repeat: Infinity, duration: 3 }}
        className="glow-circle heart-pulsing"
        style={{ 
          background: 'linear-gradient(135deg, #FF6B6B, #FF8E8E)',
          width: '100px',
          height: '100px',
          borderRadius: '30px'
        }}
      >
        <MessageCircle size={48} color="#fff" fill="#fff" />
      </motion.div>
    </div>
    <h2 className="premium-title">Share the Love</h2>
    <div className="love-popup-box glass-panel" style={{ padding: '2rem', background: 'rgba(255, 107, 107, 0.05)', border: '1px dashed #FF6B6B' }}>
      <p className="p-message-bold" style={{ color: '#E54848', fontSize: '1.2rem' }}>
        "A small 'Thinking of you' can brighten a whole world. Your heart has so much to give!" 💖
      </p>
    </div>
    <p className="p-desc" style={{ marginTop: '1.5rem', fontWeight: 500 }}>
      Texting a friend is like sending a digital hug. Who could use a hug from you right now?
    </p>
    <div className="premium-controls vertical">
      <motion.button 
        whileHover={{ scale: 1.05 }}
        whileTap={{ scale: 0.95 }}
        className="p-btn primary large" 
        style={{ background: '#FF6B6B', boxShadow: '0 10px 20px rgba(255, 107, 107, 0.3)' }}
        onClick={() => window.open('https://web.whatsapp.com', '_blank')}
      >
        <Send size={20} />
        <span>Send a Digital Hug</span>
      </motion.button>
      <button className="p-btn ghost" onClick={onClose}>Maybe in a little bit</button>
    </div>
  </div>
);

const GardenStopwatchView = ({ onClose }) => {
  const [seconds, setSeconds] = useState(0);
  const [isActive, setIsActive] = useState(false);
  const [isFinished, setIsFinished] = useState(false);

  useEffect(() => {
    let interval = null;
    if (isActive) {
      interval = setInterval(() => setSeconds(s => s + 1), 1000);
    }
    return () => clearInterval(interval);
  }, [isActive]);

  const getFeedback = () => {
    if (seconds < 30) return "A quick breath of air is a good start. Try to stay a bit longer next time!";
    if (seconds < 120) return "Great job! You've given your brain a real oxygen boost. You should feel more refreshed.";
    if (seconds < 300) return "Excellent! This long exposure to nature has significantly lowered your stress levels.";
    return "Amazing! You've reached a state of deep environmental grounding. Your mind is reset.";
  };

  const formatTime = (s) => {
    const mins = Math.floor(s / 60);
    const secs = s % 60;
    return `${mins}:${secs < 10 ? '0' : ''}${secs}`;
  };

  return (
    <div className="premium-view garden-view">
      {!isFinished ? (
        <>
          <div className="garden-visual cute-garden">
            <motion.div 
              animate={{ 
                rotate: 360,
                scale: [1, 1.1, 1]
              }} 
              transition={{ 
                rotate: { duration: 20, repeat: Infinity, ease: "linear" },
                scale: { duration: 4, repeat: Infinity, ease: "easeInOut" }
              }}
              className="sun-box"
            >
              <Sun size={60} color="#FBBF24" fill="#FDE68A" />
            </motion.div>
            
            <div className="flowers-row">
              {[...Array(5)].map((_, i) => (
                <motion.div 
                  key={i}
                  animate={{ rotate: [0, 10, -10, 0] }}
                  transition={{ repeat: Infinity, duration: 2 + i, delay: i * 0.2 }}
                  className="cute-flower"
                >
                  🌸
                </motion.div>
              ))}
            </div>

            <div className="trees-row">
              <motion.div animate={{ skewX: [0, 5, 0] }} transition={{ repeat: Infinity, duration: 4 }}><TreePine size={40} color="#34D399" /></motion.div>
              <motion.div animate={{ skewX: [0, -5, 0] }} transition={{ repeat: Infinity, duration: 5 }}><TreePine size={56} color="#10B981" /></motion.div>
              <motion.div animate={{ skewX: [0, 3, 0] }} transition={{ repeat: Infinity, duration: 3 }}><TreePine size={40} color="#34D399" /></motion.div>
            </div>
          </div>

          <h2 className="premium-title">Breathe the Outdoors</h2>
          
          <div className="stopwatch-box">
            <Timer size={20} className="stopwatch-icon" />
            <span className="stopwatch-val">{formatTime(seconds)}</span>
          </div>

          <div className="premium-controls">
            <button className={`p-btn primary ${isActive ? 'active' : ''}`} onClick={() => setIsActive(!isActive)}>
              {isActive ? <Pause size={20} /> : <Play size={20} />}
              <span>{isActive ? 'Stop' : 'Start Timer'}</span>
            </button>
            {seconds > 0 && !isActive && (
              <button className="p-btn secondary-success" onClick={() => setIsFinished(true)}>
                Complete Session
              </button>
            )}
          </div>
        </>
      ) : (
        <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} className="garden-feedback">
          <div className="feedback-badge">
            <Sparkles size={32} color="#059669" />
          </div>
          <h2 className="premium-title">Session Complete</h2>
          <div className="time-summary">You spent <strong>{formatTime(seconds)}</strong> outdoors.</div>
          <p className="p-message-feedback">{getFeedback()}</p>
          <button className="p-btn primary large" onClick={onClose}>Return to Dashboard</button>
        </motion.div>
      )}
    </div>
  );
};

const AdvancedReflectionView = ({ onClose }) => {
  const [step, setStep] = useState(0);
  const [answers, setAnswers] = useState([]);
  
  const questions = [
    {
      q: "Scan your mind. How is your energy today?",
      options: [
        { val: "Dim", label: "Low & Quiet", icon: "🌙" },
        { val: "Steady", label: "Calm & Stable", icon: "⚖️" },
        { val: "Restless", label: "Tense & Active", icon: "⚡" },
        { val: "Radiant", label: "High & Positive", icon: "☀️" }
      ],
      feedback: {
        "Dim": "It's okay to retreat. Quiet days are for internal growth.",
        "Steady": "Balance is your superpower. Maintain this rhythm.",
        "Restless": "Tension is just energy waiting for direction.",
        "Radiant": "Your light is contagious. Share it or save it for a challenge."
      }
    },
    {
      q: "Where is your focus most needed right now?",
      options: [
        { val: "Self", label: "Inner Healing", icon: "🌱" },
        { val: "Connection", label: "Relationships", icon: "🤝" },
        { val: "Action", label: "Work & Goals", icon: "🎯" },
        { val: "Rest", label: "Deep Recovery", icon: "💤" }
      ],
      feedback: {
        "Self": "Prioritizing yourself is not selfish, it's essential.",
        "Connection": "Human bonds are the strongest emotional anchors.",
        "Action": "Focus on the smallest possible next step.",
        "Rest": "Sleep is the greatest force multiplier for health."
      }
    }
  ];

  const handleSelect = (option) => {
    setAnswers([...answers, option]);
    setStep(step + 1);
  };

  return (
    <div className="premium-view reflection-view">
      <AnimatePresence mode="wait">
        {step < questions.length ? (
          <motion.div 
            key={step}
            initial={{ opacity: 0, x: 20 }}
            animate={{ opacity: 1, x: 0 }}
            exit={{ opacity: 0, x: -20 }}
            className="question-container"
          >
            <span className="step-indicator">STEP {step + 1} OF {questions.length}</span>
            <h2 className="premium-title">{questions[step].q}</h2>
            <div className="premium-options">
              {questions[step].options.map(opt => (
                <button key={opt.val} className="p-option-card" onClick={() => handleSelect(opt.val)}>
                  <span className="p-opt-icon">{opt.icon}</span>
                  <span className="p-opt-label">{opt.label}</span>
                </button>
              ))}
            </div>
          </motion.div>
        ) : (
          <motion.div 
            initial={{ opacity: 0, scale: 0.9 }}
            animate={{ opacity: 1, scale: 1 }}
            className="summary-container"
          >
            <div className="summary-accent">
              <motion.div
                animate={{ rotate: 360 }}
                transition={{ duration: 20, repeat: Infinity, ease: "linear" }}
              >
                <Sparkles size={48} color="var(--color-primary)" />
              </motion.div>
            </div>
            <h2 className="premium-title">Your Insights</h2>
            <div className="insight-cards">
              {answers.map((ans, i) => (
                <motion.div 
                  key={i} 
                  initial={{ opacity: 0, y: 20 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{ delay: i * 0.2 }}
                  className="insight-p-card glass-panel"
                  style={{ padding: '1.5rem', marginBottom: '1rem', borderLeft: '4px solid var(--color-primary)' }}
                >
                  <p style={{ fontStyle: 'italic', color: 'var(--text-main)', fontSize: '1.1rem' }}>
                    “{questions[i].feedback[ans]}”
                  </p>
                </motion.div>
              ))}
            </div>
            <button className="p-btn primary large" style={{ width: '100%', marginTop: '1.5rem' }} onClick={onClose}>
              Finish Reflection
            </button>
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
};

const AdvancedBreathingView = () => {
  const [phase, setPhase] = useState('Inhale');
  const [count, setCount] = useState(4);
  const [breathCount, setBreathCount] = useState(1);

  useEffect(() => {
    const timer = setInterval(() => {
      setCount((prev) => {
        if (prev === 1) {
          if (phase === 'Exhale') {
            if (breathCount < 3) {
              setBreathCount(breathCount + 1);
              setPhase('Inhale');
            } else {
              setPhase('Done!');
              clearInterval(timer);
            }
          } else {
            setPhase('Exhale');
          }
          return 4;
        }
        return prev - 1;
      });
    }, 1000);
    return () => clearInterval(timer);
  }, [phase, breathCount]);


  return (
    <div className="premium-view breathing-view">
      <div className="breathing-stage">
        <motion.div 
          animate={{ 
            scale: phase === 'Inhale' ? [1, 1.6] : [1.6, 1],
            opacity: phase === 'Inhale' ? [0.4, 0.8] : [0.8, 0.4]
          }}
          transition={{ duration: 4, ease: "easeInOut" }}
          className="b-ring outer"
        />
        <motion.div 
          animate={{ 
            scale: phase === 'Inhale' ? [1, 1.4] : [1.4, 1],
          }}
          transition={{ duration: 4, ease: "easeInOut" }}
          className="b-ring middle"
        />
        <div className="b-core cute-core">
          <AnimatePresence mode="wait">
            <motion.div 
              key={phase}
              initial={{ opacity: 0, scale: 0.8 }}
              animate={{ opacity: 1, scale: 1 }}
              exit={{ opacity: 0, scale: 1.2 }}
              className="b-text-wrap"
            >
              <span className="b-phase">{phase === 'Done!' ? '✨' : phase.toUpperCase()}</span>
              {phase !== 'Done!' && <span className="b-count">{count}</span>}
            </motion.div>
          </AnimatePresence>
        </div>
      </div>
      <div className="breath-dots">
        {[1, 2, 3].map(i => (
          <motion.div 
            key={i} 
            animate={i <= breathCount ? { scale: [1, 1.3, 1], backgroundColor: '#8A64D6' } : {}}
            className={`breath-dot ${i <= breathCount ? 'active' : ''}`}
          />
        ))}
      </div>
      <h2 className="premium-title">3 Gentle Breaths</h2>
      <p className="p-desc">Deep breaths are like small resets for your soul. Just 3 more to go!</p>
    </div>
  );
};

export default RecommendationActionModal;
