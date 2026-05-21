import { Routes, Route, Navigate, useLocation } from 'react-router-dom';
import Auth from './pages/Auth';
import Dashboard from './pages/Dashboard';
import ResetPassword from './pages/ResetPassword';
import LandingPage from './pages/LandingPage';
import Profile from './pages/Profile';
import HabitManagement from './pages/HabitManagement';
import NotificationsPage from './pages/NotificationsPage.notification';
import Mood from './pages/Mood';
import Analytics from './pages/Analytics';
import Recommendations from './pages/Recommendations';

function App() {
  const location = useLocation();
  const isAuthenticated = !!localStorage.getItem('user');

  return (
    <div className="app-container">
      <Routes>
        <Route path="/" element={isAuthenticated ? <Navigate to="/dashboard" /> : <LandingPage />} />
        <Route path="/auth" element={isAuthenticated ? <Navigate to="/dashboard" /> : <Auth />} />
        <Route path="/dashboard" element={isAuthenticated ? <Dashboard /> : <Navigate to="/auth" />} />
        <Route path="/profile" element={isAuthenticated ? <Profile /> : <Navigate to="/auth" />} />
        <Route path="/habits" element={isAuthenticated ? <HabitManagement /> : <Navigate to="/auth" />} />
        <Route path="/mood" element={isAuthenticated ? <Mood /> : <Navigate to="/auth" />} />
        <Route path="/notifications" element={isAuthenticated ? <NotificationsPage /> : <Navigate to="/auth" />} />
        <Route path="/analytics" element={isAuthenticated ? <Analytics /> : <Navigate to="/auth" />} />
        <Route path="/recommendations" element={isAuthenticated ? <Recommendations /> : <Navigate to="/auth" />} />
        <Route path="/reset-password/:token" element={<ResetPassword />} />
      </Routes>
    </div>
  );
}

export default App;
