import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import API_BASE from '../utils/api.js';

function ResetPassword() {
  const { token } = useParams();
  const navigate = useNavigate();
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (newPassword !== confirmPassword) {
      return setError('Passwords do not match');
    }
    setError('');
    setLoading(true);

    try {
      const response = await fetch(`${API_BASE}/api/auth/reset-password`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ token, newPassword })
      });

      if (response.ok) {
        setSuccess('Password reset successful! Redirecting to login...');
        setTimeout(() => navigate('/auth'), 3000);
      } else {
        const msg = await response.text();
        setError(msg || 'Failed to reset password');
      }
    } catch (err) {
      setError('Could not connect to the server');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="landing">
      <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh', width: '100vw', backgroundColor: 'var(--bg-main)' }}>
        <div className="card" style={{ width: '100%', maxWidth: '400px', padding: '2rem' }}>
          <div className="text-center mb-4">
            <h2 style={{ color: 'var(--color-primary)' }}>Mello</h2>
            <p>Create a new password</p>
          </div>

          {error && <p style={{ color: 'red', textAlign: 'center', marginBottom: '1rem', fontSize: '0.9rem' }}>{error}</p>}
          {success && <p style={{ color: 'green', textAlign: 'center', marginBottom: '1rem', fontSize: '0.9rem' }}>{success}</p>}

          <form onSubmit={handleSubmit}>
            <div className="input-group">
              <label>New Password</label>
              <input 
                type="password" 
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                placeholder="••••••••" 
                required 
              />
            </div>
            <div className="input-group">
              <label>Confirm New Password</label>
              <input 
                type="password" 
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                placeholder="••••••••" 
                required 
              />
            </div>

            <button type="submit" className="btn btn-primary" disabled={loading || success} style={{ width: '100%', marginTop: '1rem' }}>
              {loading ? 'Resetting...' : 'Reset Password'}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}

export default ResetPassword;
