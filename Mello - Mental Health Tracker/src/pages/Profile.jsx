import React, { useState, useEffect, useRef } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { User as UserIcon, Mail, Edit2, Save, X, Camera, Shield, Lock, Key } from 'lucide-react';
import Sidebar from '../components/Sidebar';
import { useNavigate } from 'react-router-dom';
import API_BASE from '../utils/api.js';

function Profile() {
  const [user, setUser] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [isChangingPassword, setIsChangingPassword] = useState(false);
  const [formData, setFormData] = useState({ fullName: '', email: '', profilePicture: '' });
  const [passwordData, setPasswordData] = useState({ oldPassword: '', newPassword: '', confirmPassword: '' });
  const [isSaving, setIsSaving] = useState(false);
  const [message, setMessage] = useState({ type: '', text: '' });
  const fileInputRef = useRef(null);
  const navigate = useNavigate();

  useEffect(() => {
    const savedUser = localStorage.getItem('user');
    if (savedUser) {
      const parsedUser = JSON.parse(savedUser);
      setUser(parsedUser);
      setFormData({
        fullName: parsedUser.fullName,
        email: parsedUser.email,
        profilePicture: parsedUser.profilePicture || ''
      });
    } else {
      navigate('/auth');
    }
  }, [navigate]);

  const handleUpdate = async (e) => {
    if (e) e.preventDefault();
    setIsSaving(true);
    setMessage({ type: '', text: '' });

    try {
      const response = await fetch(`${API_BASE}/api/users/${user.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData)
      });

      if (response.ok) {
        const updatedUser = await response.json();
        localStorage.setItem('user', JSON.stringify(updatedUser));
        setUser(updatedUser);
        setIsEditing(false);
        setMessage({ type: 'success', text: 'Profile updated successfully!' });
        setTimeout(() => setMessage({ type: '', text: '' }), 3000);
      } else {
        const errText = await response.text();
        setMessage({ type: 'error', text: errText || 'Failed to update profile.' });
      }
    } catch (err) {
      console.error(err);
      setMessage({ type: 'error', text: 'An error occurred. Please try again.' });
    } finally {
      setIsSaving(false);
    }
  };

  const handleChangePassword = async (e) => {
    e.preventDefault();
    if (passwordData.newPassword !== passwordData.confirmPassword) {
      setMessage({ type: 'error', text: 'Passwords do not match.' });
      return;
    }
    setIsSaving(true);
    try {
      const response = await fetch(`${API_BASE}/api/users/${user.id}/change-password`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          oldPassword: passwordData.oldPassword,
          newPassword: passwordData.newPassword
        })
      });
      if (response.ok) {
        setMessage({ type: 'success', text: 'Password changed successfully!' });
        setIsChangingPassword(false);
        setPasswordData({ oldPassword: '', newPassword: '', confirmPassword: '' });
        setTimeout(() => setMessage({ type: '', text: '' }), 3000);
      } else {
        const error = await response.text();
        setMessage({ type: 'error', text: error || 'Failed to change password.' });
      }
    } catch (err) {
      console.error(err);
      setMessage({ type: 'error', text: 'An error occurred.' });
    } finally {
      setIsSaving(false);
    }
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setFormData({ ...formData, profilePicture: reader.result });
      };
      reader.readAsDataURL(file);
    }
  };

  if (!user) return null;

  return (
    <div className="dashboard">
      <div className="app-container">
        <Sidebar />
        <div className="content-area" style={{ padding: '4rem', background: 'var(--bg-main)', display: 'flex', justifyContent: 'center' }}>
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="glass-panel"
            style={{ maxWidth: '850px', width: '100%', height: 'fit-content', padding: '4rem', borderRadius: '40px' }}
          >
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '4rem' }}>
              <div>
                <h1 style={{ fontSize: '3rem', fontWeight: 800, margin: 0, color: 'var(--text-main)' }}>Your Profile</h1>
                <p style={{ fontSize: '1.2rem', color: 'var(--text-muted)' }}>Manage your personal sanctuary settings</p>
              </div>
              {!isEditing && !isChangingPassword && (
                <button
                  onClick={() => setIsEditing(true)}
                  className="glass-button"
                  style={{ backgroundColor: 'var(--color-primary)', color: 'white', border: 'none', padding: '0.75rem 1.5rem' }}
                >
                  <Edit2 size={18} style={{ marginRight: '0.5rem' }} /> Edit Profile
                </button>
              )}
              {(isEditing || isChangingPassword) && (
                <button
                  onClick={() => {
                    setIsEditing(false);
                    setIsChangingPassword(false);
                    setMessage({ type: '', text: '' });
                    // Reset formData to current user state if cancelling
                    setFormData({ fullName: user.fullName, email: user.email, profilePicture: user.profilePicture || '' });
                  }}
                  className="glass-button"
                  style={{ backgroundColor: 'rgba(0,0,0,0.05)', color: 'var(--text-main)', border: 'none', padding: '0.75rem 1.5rem' }}
                >
                  <X size={18} style={{ marginRight: '0.5rem' }} /> Cancel
                </button>
              )}
            </div>

            <div style={{ display: 'grid', gridTemplateColumns: '280px 1fr', gap: '4rem' }}>
              {/* Avatar Section */}
              <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '1.5rem' }}>
                <div style={{
                  width: '220px', height: '220px', borderRadius: '50%', background: 'var(--color-primary-light)',
                  display: 'flex', justifyContent: 'center', alignItems: 'center', position: 'relative',
                  boxShadow: '0 20px 40px rgba(138, 100, 214, 0.2)', border: '8px solid white',
                  overflow: 'hidden'
                }}>
                  {formData.profilePicture ? (
                    <img src={formData.profilePicture} alt="Profile" style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
                  ) : (
                    <UserIcon size={100} color="var(--color-primary)" />
                  )}
                  {isEditing && (
                    <motion.button
                      initial={{ opacity: 0, scale: 0.8 }} animate={{ opacity: 1, scale: 1 }}
                      onClick={() => fileInputRef.current.click()}
                      style={{
                        position: 'absolute', inset: 0, width: '100%', height: '100%',
                        background: 'rgba(0,0,0,0.3)', color: 'white', display: 'flex',
                        flexDirection: 'column', justifyContent: 'center', alignItems: 'center',
                        gap: '0.5rem', borderRadius: '50%', border: 'none', cursor: 'pointer'
                      }}
                    >
                      <Camera size={32} />
                      <span style={{ fontWeight: 700, fontSize: '0.9rem' }}>Change Photo</span>
                    </motion.button>
                  )}
                  <input
                    type="file"
                    ref={fileInputRef}
                    onChange={handleFileChange}
                    accept="image/*"
                    style={{ display: 'none' }}
                  />
                </div>
                <p style={{ fontWeight: 700, fontSize: '1.5rem', color: 'var(--text-main)', margin: 0 }}>{user.fullName}</p>
                <div style={{ padding: '0.5rem 1.25rem', background: 'var(--color-green-soft)', borderRadius: 'var(--radius-full)', color: 'var(--color-green-bright)', fontSize: '0.9rem', fontWeight: 800 }}>
                  ACTIVE MEMBER
                </div>
              </div>

              {/* Content Section */}
              <div style={{ display: 'flex', flexDirection: 'column', gap: '2rem' }}>
                <AnimatePresence mode="wait">
                  {message.text && (
                    <motion.div
                      initial={{ opacity: 0, height: 0 }}
                      animate={{ opacity: 1, height: 'auto' }}
                      exit={{ opacity: 0, height: 0 }}
                      style={{
                        padding: '1rem', borderRadius: 'var(--radius-md)',
                        backgroundColor: message.type === 'success' ? 'var(--color-green-soft)' : 'var(--color-red)',
                        color: message.type === 'success' ? '#2E7D32' : '#C62828',
                        fontWeight: 600, textAlign: 'center', marginBottom: '1rem'
                      }}
                    >
                      {message.text}
                    </motion.div>
                  )}
                </AnimatePresence>

                {isChangingPassword ? (
                  <motion.form
                    initial={{ opacity: 0, x: 20 }} animate={{ opacity: 1, x: 0 }}
                    onSubmit={handleChangePassword}
                    style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}
                  >
                    <h3 style={{ fontSize: '1.5rem', marginBottom: '1rem' }}>Change Password</h3>
                    <div className="input-group">
                      <label style={{ color: 'var(--text-muted)', fontSize: '0.75rem', fontWeight: 800 }}>Old Password</label>
                      <div style={{ position: 'relative' }}>
                        <Lock size={18} style={{ position: 'absolute', left: '1rem', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
                        <input
                          type="password" required
                          value={passwordData.oldPassword}
                          onChange={(e) => setPasswordData({ ...passwordData, oldPassword: e.target.value })}
                          style={{ width: '100%', padding: '1rem 1rem 1rem 3rem', borderRadius: 'var(--radius-md)', border: '2px solid var(--color-primary)', background: 'white' }}
                        />
                      </div>
                    </div>
                    <div className="input-group">
                      <label style={{ color: 'var(--text-muted)', fontSize: '0.75rem', fontWeight: 800 }}>New Password</label>
                      <div style={{ position: 'relative' }}>
                        <Key size={18} style={{ position: 'absolute', left: '1rem', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
                        <input
                          type="password" required
                          value={passwordData.newPassword}
                          onChange={(e) => setPasswordData({ ...passwordData, newPassword: e.target.value })}
                          style={{ width: '100%', padding: '1rem 1rem 1rem 3rem', borderRadius: 'var(--radius-md)', border: '2px solid var(--color-primary)', background: 'white' }}
                        />
                      </div>
                    </div>
                    <div className="input-group">
                      <label style={{ color: 'var(--text-muted)', fontSize: '0.75rem', fontWeight: 800 }}>Confirm New Password</label>
                      <div style={{ position: 'relative' }}>
                        <Key size={18} style={{ position: 'absolute', left: '1rem', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
                        <input
                          type="password" required
                          value={passwordData.confirmPassword}
                          onChange={(e) => setPasswordData({ ...passwordData, confirmPassword: e.target.value })}
                          style={{ width: '100%', padding: '1rem 1rem 1rem 3rem', borderRadius: 'var(--radius-md)', border: '2px solid var(--color-primary)', background: 'white' }}
                        />
                      </div>
                    </div>
                    <button type="submit" disabled={isSaving} className="glass-button" style={{ backgroundColor: 'var(--color-primary)', color: 'white', padding: '1.25rem' }}>
                      {isSaving ? 'Updating...' : 'Update Password'}
                    </button>
                  </motion.form>
                ) : (
                  <form onSubmit={handleUpdate} style={{ display: 'flex', flexDirection: 'column', gap: '2rem' }}>
                    <div className="input-group">
                      <label style={{ color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.1em', fontSize: '0.75rem', fontWeight: 800 }}>Full Name</label>
                      <div style={{ position: 'relative' }}>
                        <UserIcon size={18} style={{ position: 'absolute', left: '1rem', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
                        <input
                          type="text"
                          value={formData.fullName}
                          onChange={(e) => setFormData({ ...formData, fullName: e.target.value })}
                          disabled={!isEditing}
                          style={{
                            width: '100%', padding: '1rem 1rem 1rem 3rem', borderRadius: 'var(--radius-md)',
                            border: isEditing ? '2px solid var(--color-primary)' : '2px solid transparent',
                            background: isEditing ? 'white' : 'rgba(0,0,0,0.02)',
                            fontSize: '1.1rem', fontWeight: 600, color: 'var(--text-main)', transition: 'all 0.3s ease'
                          }}
                        />
                      </div>
                    </div>

                    <div className="input-group">
                      <label style={{ color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.1em', fontSize: '0.75rem', fontWeight: 800 }}>Email Address</label>
                      <div style={{ position: 'relative' }}>
                        <Mail size={18} style={{ position: 'absolute', left: '1rem', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-muted)' }} />
                        <input
                          type="email"
                          value={formData.email}
                          onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                          disabled={!isEditing}
                          style={{
                            width: '100%', padding: '1rem 1rem 1rem 3rem', borderRadius: 'var(--radius-md)',
                            border: isEditing ? '2px solid var(--color-primary)' : '2px solid transparent',
                            background: isEditing ? 'white' : 'rgba(0,0,0,0.02)',
                            fontSize: '1.1rem', fontWeight: 600, color: 'var(--text-main)', transition: 'all 0.3s ease'
                          }}
                        />
                      </div>
                    </div>

                    {isEditing && (
                      <motion.button
                        initial={{ opacity: 0, y: 10 }}
                        animate={{ opacity: 1, y: 0 }}
                        type="submit"
                        disabled={isSaving}
                        className="glass-button"
                        style={{
                          backgroundColor: 'var(--color-primary)', color: 'white', border: 'none',
                          padding: '1.25rem', fontSize: '1.1rem', marginTop: '1rem'
                        }}
                      >
                        {isSaving ? 'Saving Changes...' : <><Save size={20} style={{ marginRight: '0.75rem' }} /> Save Changes</>}
                      </motion.button>
                    )}

                    {!isEditing && (
                      <div style={{ marginTop: '2rem', display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                        <div
                          className="glass-panel"
                          onClick={() => setIsChangingPassword(true)}
                          style={{ padding: '1.5rem', background: 'rgba(0,0,0,0.02)', display: 'flex', justifyContent: 'space-between', alignItems: 'center', cursor: 'pointer' }}
                        >
                          <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
                            <Shield size={20} color="var(--color-primary)" />
                            <div>
                              <p style={{ margin: 0, fontWeight: 700, color: 'var(--text-main)' }}>Security</p>
                              <p style={{ margin: 0, fontSize: '0.875rem' }}>Update your password and security settings</p>
                            </div>
                          </div>
                          <ChevronRight size={20} color="var(--text-muted)" />
                        </div>
                      </div>
                    )}
                  </form>
                )}
              </div>
            </div>
          </motion.div>
        </div>
      </div>
    </div>
  );
}

const ChevronRight = ({ size, color }) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" stroke={color} strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <path d="M9 18l6-6-6-6" />
  </svg>
);

export default Profile;
