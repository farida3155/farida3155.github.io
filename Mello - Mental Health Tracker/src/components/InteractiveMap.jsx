import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { Map, Smile, CheckCircle2, BarChart2, Lightbulb, Bell } from 'lucide-react';
import mapBase from '../assets/map-base.png';

// Cute Mello Character SVG Component for perfect transparency
const MelloCharacter = ({ isMoving }) => (
  <svg viewBox="0 0 100 100" className="mello-svg" style={{ width: '100%', height: '100%' }}>
    {/* Body Shadow */}
    <ellipse cx="50" cy="85" rx="30" ry="10" fill="rgba(0,0,0,0.15)" />
    
    {/* Main Body */}
    <path 
      d="M 25 40 C 25 15, 75 15, 75 40 L 75 70 C 75 85, 25 85, 25 70 Z" 
      fill="#A7F3D0" 
      stroke="#059669" 
      strokeWidth="2"
    />
    
    {/* Belly */}
    <ellipse cx="50" cy="65" rx="18" ry="12" fill="#D1FAE5" />
    
    {/* Horns */}
    <path d="M 30 25 L 20 15" stroke="#059669" strokeWidth="6" strokeLinecap="round" />
    <path d="M 70 25 L 80 15" stroke="#059669" strokeWidth="6" strokeLinecap="round" />
    
    {/* Eyes */}
    <circle cx="40" cy="45" r="3" fill="#064E3B" />
    <circle cx="60" cy="45" r="3" fill="#064E3B" />
    
    {/* Mouth */}
    <path 
      d="M 45 52 Q 50 56, 55 52" 
      fill="none" 
      stroke="#064E3B" 
      strokeWidth="2" 
      strokeLinecap="round" 
    />
    
    {/* Blush */}
    <circle cx="33" cy="50" r="4" fill="#6EE7B7" opacity="0.6" />
    <circle cx="67" cy="50" r="4" fill="#6EE7B7" opacity="0.6" />
  </svg>
);

const islands = [
  { 
    id: 'core', 
    name: 'Sanctuary Peak', 
    icon: <Map size={24} />, 
    color: '#8A64D6', 
    pos: [62, 35], 
    path: '/dashboard',
    description: 'Your central hub for peace.'
  },
  { 
    id: 'mood', 
    name: 'Mood Valley', 
    icon: <Smile size={20} />, 
    color: '#A78BFA', 
    pos: [18, 22], 
    path: '/mood',
    description: 'Reflect on your emotional flow.'
  },
  { 
    id: 'habits', 
    name: 'Habit Forest', 
    icon: <CheckCircle2 size={20} />, 
    color: '#10B981', 
    pos: [28, 55], 
    path: '/habits',
    description: 'Grow your daily routines.'
  },
  { 
    id: 'analytics', 
    name: 'Wisdom Peak', 
    icon: <BarChart2 size={20} />, 
    color: '#3B82F6', 
    pos: [12, 82], 
    path: '/analytics',
    description: 'Analyze your journey.'
  },
  { 
    id: 'grace', 
    name: 'Grace Springs', 
    icon: <Lightbulb size={20} />, 
    color: '#FBBF24', 
    pos: [78, 85], 
    path: '/recommendations',
    description: 'Fresh insights daily.'
  },
  { 
    id: 'notifications', 
    name: 'Echo Tower', 
    icon: <Bell size={20} />, 
    color: '#F472B6', 
    pos: [88, 22], 
    path: '/notifications',
    description: 'Stay mindful of your tasks.'
  },
];


const InteractiveMap = () => {
  const navigate = useNavigate();
  const [characterPos, setCharacterPos] = useState({ x: 62, y: 35 });
  const [isMoving, setIsMoving] = useState(false);
  const [targetPath, setTargetPath] = useState(null);

  const handleNodeClick = (island) => {
    if (isMoving) return;
    
    // Start moving
    setIsMoving(true);
    setTargetPath(island.path);
    setCharacterPos({ x: island.pos[0], y: island.pos[1] });
  };

  const onMoveEnd = () => {
    if (isMoving && targetPath) {
      setIsMoving(false);
      // Wait a tiny bit for the settle animation before navigating
      setTimeout(() => {
        navigate(targetPath);
      }, 300);
    }
  };

  return (
    <div className="map-outer-wrapper">
      <div className="map-container">
        {/* Map Background */}
        <img 
          src={mapBase} 
          alt="Sanctuary Map" 
          className="map-background"
        />
        
        {/* Paper Texture Overlay */}
        <div className="map-texture-overlay"></div>
        
        {/* SVG Connections (Constellations) */}
        <svg className="map-svg-overlay" viewBox="0 0 100 100" preserveAspectRatio="none">
          <motion.path
            d="M 62 35 L 18 22 M 62 35 L 28 55 M 28 55 L 12 82 M 62 35 L 78 85 M 62 35 L 88 22"
            stroke="rgba(255,255,255,0.4)"
            strokeWidth="0.2"
            strokeDasharray="1 1"
            fill="none"
            initial={{ pathLength: 0, opacity: 0 }}
            whileInView={{ pathLength: 1, opacity: 1 }}
            transition={{ duration: 2, ease: "easeInOut" }}
          />
        </svg>

        {/* Bottom Vignette */}
        <div className="map-vignette"></div>
      </div>

      {/* Interactive Nodes Layer (Allows Overflow) */}
      <div className="map-nodes-layer">
        {islands.map((island, idx) => {
          const isTopRow = island.pos[1] < 25;
          const isLeftEdge = island.pos[0] < 20;
          const isRightEdge = island.pos[0] > 80;

          return (
            <motion.div
              key={island.id}
              initial={{ opacity: 0, scale: 0.8 }}
              whileInView={{ opacity: 1, scale: 1 }}
              transition={{ delay: idx * 0.1 }}
              className="map-node-wrapper"
              style={{ 
                left: `${island.pos[0]}%`, 
                top: `${island.pos[1]}%`,
              }}
            >
              <motion.div
                whileHover={{ scale: 1.1, y: -5 }}
                className="map-node"
                onClick={() => handleNodeClick(island)}
              >
                {/* Pulsing Aura */}
                <div 
                  className="map-node-pulse"
                  style={{ backgroundColor: island.color }}
                ></div>
                
                {/* Node Icon */}
                <div 
                  className="map-node-icon"
                  style={{ 
                    color: island.color,
                    border: `2px solid ${island.color}44`,
                  }}
                >
                  {island.icon}
                </div>

                {/* Label */}
                <div className="map-node-label">
                  <span>{island.name}</span>
                </div>
                
                {/* Tooltip */}
                <div 
                  className={`map-node-tooltip ${isTopRow ? 'tooltip-bottom' : ''} ${isLeftEdge ? 'tooltip-left' : ''} ${isRightEdge ? 'tooltip-right' : ''}`}
                >
                  <p>{island.description}</p>
                  <div className="map-tooltip-arrow"></div>
                </div>
              </motion.div>
            </motion.div>
          );
        })}

        {/* Mello Character (Moved to bottom of layer for highest stacking) */}
        <motion.div
          className="map-character-wrapper"
          animate={{ 
            left: `${characterPos.x}%`, 
            top: `${characterPos.y}%`,
          }}
          transition={{ 
            type: "spring",
            stiffness: 40,
            damping: 15,
            mass: 1
          }}
          onAnimationComplete={onMoveEnd}
          style={{ 
            position: 'absolute',
            zIndex: 100, // Explicitly high z-index
            pointerEvents: 'none',
            transform: 'translate(-50%, -50%)',
            width: '64px',
            height: '64px',
          }}
        >
          <motion.div
            animate={isMoving ? {
              y: [0, -12, 0],
              rotate: [0, -8, 8, 0],
              scaleY: [1, 0.85, 1.15, 1],
            } : {
              y: [0, -5, 0],
            }}
            transition={{
              duration: isMoving ? 0.35 : 3,
              repeat: Infinity,
              ease: "easeInOut"
            }}
            style={{ 
              width: '100%', 
              height: '100%',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
            }}
          >
            <MelloCharacter isMoving={isMoving} />
          </motion.div>
        </motion.div>
      </div>
    </div>
  );
};

export default InteractiveMap;


