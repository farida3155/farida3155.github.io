import React, { useRef } from 'react';
import { motion, useScroll, useTransform } from 'framer-motion';
import { useNavigate } from 'react-router-dom';
import { Heart, Smile, Sparkles, Map, CheckCircle2, ArrowRight, Shield, Zap } from 'lucide-react';
import { Canvas } from '@react-three/fiber';
import { Environment } from '@react-three/drei';
import BreathingOrb from '../components/BreathingOrb';

function LandingPage() {
  const navigate = useNavigate();
  const containerRef = useRef(null);
  const { scrollYProgress } = useScroll({ container: containerRef });

  const handleCTA = (e) => {
    if (e) e.stopPropagation();
    navigate('/auth');
  };

  return (
    <div className="landing">
      <div className="app-container" style={{ backgroundColor: 'var(--bg-main)' }}>
        {/* 3D BACKGROUND CANVAS */}
        <div style={{ position: 'fixed', top: 0, left: 0, width: '100%', height: '100vh', zIndex: 0, background: 'linear-gradient(to bottom, #F3F1FB, #EBE5F7)', pointerEvents: 'none' }}>
          <Canvas camera={{ position: [0, 0, 4.5], fov: 45 }}>
            <ambientLight intensity={0.6} />
            <directionalLight position={[10, 10, 5]} intensity={1.5} color="#fff" />
            <directionalLight position={[-10, -10, -5]} intensity={0.5} color="#D6C8F5" />
            <Environment preset="city" />
            <BreathingOrb />
          </Canvas>
        </div>

        {/* NAVBAR */}
        <nav style={{ 
          position: 'fixed', top: 0, left: 0, width: '100%', padding: '1.5rem 4vw', 
          display: 'flex', justifyContent: 'space-between', alignItems: 'center', 
          zIndex: 100, backdropFilter: 'blur(10px)', background: 'rgba(255,255,255,0.2)' 
        }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', cursor: 'pointer' }} onClick={() => navigate('/')}>
            <div style={{ padding: '0.5rem', background: 'white', borderRadius: '12px', boxShadow: 'var(--shadow-sm)' }}>
              <Heart size={24} color="var(--color-primary)" fill="var(--color-primary-light)" />
            </div>
            <span style={{ fontSize: '1.5rem', fontWeight: 800, color: 'var(--text-main)', letterSpacing: '-0.02em' }}>Mello</span>
          </div>
          <div style={{ display: 'flex', gap: '2rem', alignItems: 'center' }}>
            <button onClick={handleCTA} className="glass-button" style={{ padding: '0.75rem 1.5rem', border: 'none', background: 'transparent' }}>Login</button>
            <button onClick={handleCTA} className="glass-button" style={{ 
              padding: '0.75rem 2rem', backgroundColor: 'var(--color-primary)', color: 'white', border: 'none' 
            }}>Get Started</button>
          </div>
        </nav>

        {/* SCROLLABLE CONTENT */}
        <div ref={containerRef} onClick={handleCTA} className="content-area scroll-snap-container no-scrollbar" style={{ zIndex: 10 }}>
          
          {/* HERO SECTION */}
          <motion.section className="scroll-snap-section" style={{ height: '100vh', display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', textAlign: 'center', padding: '0 1rem' }}>
            <motion.div
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.8, delay: 0.2 }}
            >
              <h1 style={{ fontSize: '5.5rem', fontWeight: 800, color: 'var(--text-main)', lineHeight: 1, marginBottom: '1.5rem' }}>
                Wellness, <br /><span style={{ color: 'var(--color-primary)' }}>Reimagined.</span>
              </h1>
              <p style={{ fontSize: '1.5rem', color: 'var(--text-muted)', maxWidth: '700px', margin: '0 auto 3rem' }}>
                An immersive digital sanctuary designed to help you breathe, reflect, and grow through mindful interactions.
              </p>
              <div style={{ display: 'flex', gap: '1.5rem', justifyContent: 'center' }}>
                <button onClick={handleCTA} className="glass-button" style={{ 
                  padding: '1.25rem 3rem', backgroundColor: 'var(--color-primary)', color: 'white', fontSize: '1.25rem', boxShadow: '0 10px 30px rgba(138, 100, 214, 0.3)'
                }}>
                  Start Your Journey <ArrowRight size={20} style={{ marginLeft: '0.75rem' }} />
                </button>
              </div>
            </motion.div>
          </motion.section>

          {/* FEATURES PREVIEW: THE ORB */}
          <motion.section className="scroll-snap-section" style={{ height: '100vh', display: 'flex', justifyContent: 'center', alignItems: 'center', padding: '0 8vw' }}>
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '4rem', alignItems: 'center' }}>
              <div onClick={handleCTA} style={{ cursor: 'pointer' }}>
                <div style={{ display: 'inline-flex', padding: '0.75rem', background: 'var(--color-primary-light)', borderRadius: '16px', color: 'var(--color-primary)', marginBottom: '1.5rem' }}>
                  <Zap size={32} />
                </div>
                <h2 style={{ fontSize: '3.5rem', fontWeight: 800, marginBottom: '1.5rem', color: 'var(--text-main)' }}>Guided Breathing</h2>
                <p style={{ fontSize: '1.25rem', lineHeight: 1.6, color: 'var(--text-muted)', marginBottom: '2.5rem' }}>
                  Interact with the living Breathing Orb. It synchronizes with your breath, creating a rhythmic visual guide to help you find your center in seconds.
                </p>
                <ul style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                  <li style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', fontSize: '1.1rem', fontWeight: 600 }}>
                    <CheckCircle2 size={24} color="var(--color-green-bright)" /> Stress reduction in real-time
                  </li>
                  <li style={{ display: 'flex', alignItems: 'center', gap: '0.75rem', fontSize: '1.1rem', fontWeight: 600 }}>
                    <CheckCircle2 size={24} color="var(--color-green-bright)" /> Haptic-inspired visual feedback
                  </li>
                </ul>
              </div>
              <div className="glass-panel" style={{ padding: '2rem', height: '400px', display: 'flex', justifyContent: 'center', alignItems: 'center', borderRadius: '40px', background: 'rgba(255,255,255,0.2)' }}>
                {/* This is a visual placeholder for the orb preview */}
                <div style={{ width: '200px', height: '200px', borderRadius: '50%', background: 'radial-gradient(circle, #D6C8F5 0%, #8A64D6 100%)', filter: 'blur(20px)', opacity: 0.6 }} className="animate-pulse-glow"></div>
              </div>
            </div>
          </motion.section>

          {/* FEATURES PREVIEW: THE MAP */}
          <motion.section className="scroll-snap-section" style={{ height: '100vh', display: 'flex', justifyContent: 'center', alignItems: 'center', padding: '0 8vw' }}>
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '4rem', alignItems: 'center' }}>
              <div className="glass-panel" style={{ padding: '1rem', borderRadius: '40px', overflow: 'hidden', height: '500px', background: 'rgba(255,255,255,0.2)' }}>
                <img src="https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&q=80&w=1000" alt="Sanctuary Map Preview" style={{ width: '100%', height: '100%', objectFit: 'cover', borderRadius: '30px', opacity: 0.8 }} />
              </div>
              <div onClick={handleCTA} style={{ cursor: 'pointer' }}>
                <div style={{ display: 'inline-flex', padding: '0.75rem', background: '#FFF2CC', borderRadius: '16px', color: '#B8860B', marginBottom: '1.5rem' }}>
                  <Map size={32} />
                </div>
                <h2 style={{ fontSize: '3.5rem', fontWeight: 800, marginBottom: '1.5rem', color: 'var(--text-main)' }}>Sanctuary Map</h2>
                <p style={{ fontSize: '1.25rem', lineHeight: 1.6, color: 'var(--text-muted)', marginBottom: '2.5rem' }}>
                  Your journey isn't linear. Navigate through your mental landscape with an interactive map that visualizes your habits, moods, and reflections as a constellation of growth.
                </p>
                <button className="glass-button" style={{ border: '2px solid var(--color-primary)', color: 'var(--color-primary)' }}>
                  Explore the Map
                </button>
              </div>
            </div>
          </motion.section>

          {/* FINAL CTA */}
          <motion.section className="scroll-snap-section" style={{ height: '100vh', display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', textAlign: 'center' }}>
            <div className="glass-panel" style={{ padding: '6rem 4rem', maxWidth: '1000px', width: '90%', borderRadius: '60px', background: 'rgba(138, 100, 214, 0.05)', border: '2px solid rgba(138, 100, 214, 0.1)' }}>
              <h2 style={{ fontSize: '4rem', fontWeight: 800, marginBottom: '2rem' }}>Ready to find your Mello?</h2>
              <p style={{ fontSize: '1.5rem', color: 'var(--text-muted)', marginBottom: '3.5rem', maxWidth: '700px', margin: '0 auto 3.5rem' }}>
                Join thousands of others who have transformed their digital experience into a source of peace.
              </p>
              <button onClick={handleCTA} className="glass-button" style={{ 
                padding: '1.5rem 4rem', backgroundColor: 'var(--text-main)', color: 'white', fontSize: '1.5rem', borderRadius: 'var(--radius-full)'
              }}>
                Join Mello for Free
              </button>
            </div>
            <p style={{ marginTop: '4rem', color: 'var(--text-muted)', fontWeight: 600 }}>
              © 2026 Mello Wellness. All rights reserved.
            </p>
          </motion.section>

        </div>
      </div>
    </div>
  );
}

export default LandingPage;
