import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion, AnimatePresence } from 'framer-motion';
import { Canvas } from '@react-three/fiber';
import CalmClouds from '../components/CalmClouds';
import API_BASE from '../utils/api.js';

function Auth() {
  const [isLogin, setIsLogin] = useState(true);
  const [isForgotPassword, setIsForgotPassword] = useState(false);
  const [isFocused, setIsFocused] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const user = localStorage.getItem('user');
    if (user && !isForgotPassword) {
      navigate('/dashboard');
    }
  }, [navigate, isForgotPassword]);

  const [formData, setFormData] = useState({
    fullName: '',
    email: '',
    password: ''
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setLoading(true);

    if (isForgotPassword) {
      try {
        const response = await fetch(`${API_BASE}/api/auth/forgot-password`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ email: formData.email })
        });
        if (response.ok) {
          const msg = await response.text();
          setSuccess(msg);
        } else {
          setError('Failed to send reset link');
        }
      } catch (err) {
        setError('Could not connect to the server');
      } finally {
        setLoading(false);
      }
      return;
    }

    const endpoint = isLogin ? '/api/auth/login' : '/api/auth/register';
    const payload = isLogin
      ? { email: formData.email, password: formData.password }
      : formData;

    try {
      const response = await fetch(`${API_BASE}${endpoint}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      if (response.ok) {
        const user = await response.json();
        localStorage.setItem('user', JSON.stringify(user));
        navigate('/dashboard');
      } else {
        const msg = await response.text();
        setError(msg || 'Authentication failed');
      }
    } catch (err) {
      setError('Could not connect to the server');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="landing">
      <div style={{ position: 'relative', width: '100vw', height: '100vh', overflow: 'hidden', backgroundColor: '#F3F1FB' }}>

        {/* 3D BACKGROUND */}
        <div style={{ position: 'absolute', top: 0, left: 0, width: '100%', height: '100%', zIndex: 0 }}>
          <Canvas>
            <CalmClouds isFocused={isFocused} />
          </Canvas>
        </div>

        {/* FOCUS OVERLAY (DIMMING) */}
        <motion.div
          animate={{ opacity: isFocused ? 0.3 : 0 }}
          transition={{ duration: 1, ease: "easeInOut" }}
          style={{
            position: 'absolute', top: 0, left: 0, width: '100%', height: '100%',
            backgroundColor: '#000', pointerEvents: 'none', zIndex: 1
          }}
        />

        {/* LOGIN PANEL */}
        <div style={{ position: 'relative', zIndex: 2, display: 'flex', justifyContent: 'center', alignItems: 'center', width: '100%', height: '100%' }}>
          <motion.div
            layout
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.8, ease: "easeOut" }}
            className="glass-panel"
            style={{
              width: '100%', maxWidth: '420px', padding: '3rem',
              borderRadius: '2rem', backdropFilter: 'blur(15px)',
              backgroundColor: 'rgba(255, 255, 255, 0.4)',
              border: '1px solid rgba(255, 255, 255, 0.5)',
              boxShadow: '0 25px 50px -12px rgba(0, 0, 0, 0.1)'
            }}
          >
            <div className="text-center mb-8">
              <motion.h1
                layout
                onClick={() => navigate('/')}
                style={{
                  margin: 0, fontSize: '2.5rem', fontWeight: 800,
                  color: 'var(--color-primary)', letterSpacing: '-0.02em',
                  cursor: 'pointer'
                }}
              >
                Mello
              </motion.h1>
              <motion.p
                layout
                style={{ color: 'var(--text-muted)', marginTop: '0.5rem', fontSize: '1.1rem' }}
              >
                {isForgotPassword
                  ? 'Reset your password'
                  : isLogin
                    ? 'Welcome back to calm.'
                    : 'Start your mindful journey.'}
              </motion.p>
            </div>

            <AnimatePresence mode="wait">
              {error && (
                <motion.p
                  initial={{ opacity: 0, height: 0 }} animate={{ opacity: 1, height: 'auto' }} exit={{ opacity: 0, height: 0 }}
                  style={{ color: '#E53E3E', textAlign: 'center', marginBottom: '1.5rem', fontSize: '0.95rem', fontWeight: 500 }}
                >
                  {error}
                </motion.p>
              )}
              {success && (
                <motion.p
                  initial={{ opacity: 0, height: 0 }} animate={{ opacity: 1, height: 'auto' }} exit={{ opacity: 0, height: 0 }}
                  style={{ color: '#38A169', textAlign: 'center', marginBottom: '1.5rem', fontSize: '0.95rem', fontWeight: 500 }}
                >
                  {success}
                </motion.p>
              )}
            </AnimatePresence>

            <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
              <AnimatePresence mode="popLayout">
                {!isLogin && !isForgotPassword && (
                  <motion.div initial={{ opacity: 0, x: -10 }} animate={{ opacity: 1, x: 0 }} exit={{ opacity: 0, x: 10 }} className="input-group">
                    <label style={{ display: 'block', marginBottom: '0.5rem', fontSize: '0.9rem', fontWeight: 600, color: 'var(--text-muted)' }}>Full Name</label>
                    <input
                      type="text" name="fullName" value={formData.fullName} onChange={handleChange}
                      onFocus={() => setIsFocused(true)} onBlur={() => setIsFocused(false)}
                      placeholder="Olive Castillo" required
                      style={{ width: '100%', padding: '1rem', borderRadius: '1rem', border: '1px solid rgba(0,0,0,0.1)', background: 'rgba(255,255,255,0.8)', fontSize: '1rem' }}
                    />
                  </motion.div>
                )}
              </AnimatePresence>

              <div className="input-group">
                <label style={{ display: 'block', marginBottom: '0.5rem', fontSize: '0.9rem', fontWeight: 600, color: 'var(--text-muted)' }}>Email</label>
                <input
                  type="email" name="email" value={formData.email} onChange={handleChange}
                  onFocus={() => setIsFocused(true)} onBlur={() => setIsFocused(false)}
                  placeholder="hello@example.com" required
                  style={{ width: '100%', padding: '1rem', borderRadius: '1rem', border: '1px solid rgba(0,0,0,0.1)', background: 'rgba(255,255,255,0.8)', fontSize: '1rem' }}
                />
              </div>

              <AnimatePresence mode="popLayout">
                {!isForgotPassword && (
                  <motion.div initial={{ opacity: 0, scale: 0.95 }} animate={{ opacity: 1, scale: 1 }} exit={{ opacity: 0, scale: 0.95 }} className="input-group">
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.5rem' }}>
                      <label style={{ fontSize: '0.9rem', fontWeight: 600, color: 'var(--text-muted)' }}>Password</label>
                      {isLogin && (
                        <button
                          type="button" onClick={() => { setIsForgotPassword(true); setError(''); setSuccess(''); }}
                          style={{ fontSize: '0.85rem', color: 'var(--color-primary)', background: 'none', border: 'none', cursor: 'pointer', fontWeight: 600 }}
                        >
                          Forgot?
                        </button>
                      )}
                    </div>
                    <input
                      type="password" name="password" value={formData.password} onChange={handleChange}
                      onFocus={() => setIsFocused(true)} onBlur={() => setIsFocused(false)}
                      placeholder="••••••••" required
                      style={{ width: '100%', padding: '1rem', borderRadius: '1rem', border: '1px solid rgba(0,0,0,0.1)', background: 'rgba(255,255,255,0.8)', fontSize: '1rem' }}
                    />
                  </motion.div>
                )}
              </AnimatePresence>

              <button
                type="submit" disabled={loading}
                className="glass-button"
                style={{
                  width: '100%', marginTop: '1rem', padding: '1.25rem', borderRadius: '1rem',
                  backgroundColor: 'var(--color-primary)', color: 'white', fontWeight: 700, fontSize: '1.1rem',
                  boxShadow: '0 10px 20px -5px rgba(138, 100, 214, 0.4)'
                }}
              >
                {loading ? 'Processing...' : (isForgotPassword ? 'Send Reset Link' : isLogin ? 'Log in' : 'Sign up')}
              </button>

              {isForgotPassword && (
                <button
                  type="button" onClick={() => { setIsForgotPassword(false); setError(''); setSuccess(''); }}
                  style={{ width: '100%', background: 'none', border: 'none', color: 'var(--text-muted)', cursor: 'pointer', fontSize: '0.9rem' }}
                >
                  Back to Login
                </button>
              )}
            </form>

            {!isForgotPassword && (
              <div className="text-center mt-8">
                <button
                  onClick={() => { setIsLogin(!isLogin); setError(''); setSuccess(''); }}
                  style={{ color: 'var(--text-muted)', fontWeight: 500, background: 'none', border: 'none', cursor: 'pointer', fontSize: '1rem' }}
                >
                  {isLogin ? "New to Mello? " : "Already registered? "}
                  <span style={{ color: 'var(--color-primary)', fontWeight: 700 }}>
                    {isLogin ? "Sign up" : "Log in"}
                  </span>
                </button>
              </div>
            )}
          </motion.div>
        </div>
      </div>
    </div>
  );
}

export default Auth;
