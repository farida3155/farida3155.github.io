import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { BarChart2, TrendingUp, Calendar, Zap, Activity } from 'lucide-react';
import Sidebar from '../components/Sidebar';
import axios from 'axios';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  Title,
  Tooltip,
  Legend,
  Filler
} from 'chart.js';
import { Line, Bar } from 'react-chartjs-2';
import '../styles/analytics.css';
import API_BASE from '../utils/api.js';

// Register ChartJS components
ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  BarElement,
  Title,
  Tooltip,
  Legend,
  Filler
);

function Analytics() {
  const [user, setUser] = useState(null);
  const [analyticsData, setAnalyticsData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const savedUser = localStorage.getItem('user');
    if (savedUser) {
      const parsedUser = JSON.parse(savedUser);
      setUser(parsedUser);
      fetchAnalytics(parsedUser.email || parsedUser.id);
    }
  }, []);

  const fetchAnalytics = async (userId) => {
    try {
      setLoading(true);
      const response = await axios.get(`${API_BASE}/api/analytics?userId=${userId}`);
      setAnalyticsData(response.data);
    } catch (err) {
      console.error('Error fetching analytics:', err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="analytics-page dashboard loading">
        <div className="app-container">
          <Sidebar />
          <div className="content-area center-content">
            <motion.div animate={{ rotate: 360 }} transition={{ repeat: Infinity, duration: 1 }}>
              <Zap size={48} color="#8A64D6" />
            </motion.div>
            <p>Gathering your insights...</p>
          </div>
        </div>
      </div>
    );
  }

  const moodChartData = {
    labels: analyticsData?.weeklyLabels || [],
    datasets: [
      {
        label: 'Mood Level',
        data: analyticsData?.weeklyMoodScores || [],
        borderColor: '#8A64D6',
        backgroundColor: 'rgba(138, 100, 214, 0.1)',
        fill: true,
        tension: 0.4,
      },
    ],
  };

  const habitChartData = {
    labels: analyticsData?.weeklyLabels || [],
    datasets: [
      {
        label: 'Activity',
        data: analyticsData?.workoutDone || [],
        backgroundColor: '#76A670',
        borderRadius: 8,
      },
    ],
  };

  return (
    <div className="analytics-page dashboard">
      <div className="app-container">
        <Sidebar />

        <div className="content-area no-scrollbar">
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="top-header"
          >
            <div>
              <h1>Your Insights</h1>
              <p>Understanding your patterns and progress.</p>
            </div>
            <div className="week-pill">
              <Calendar size={16} />
              <span>This Week</span>
            </div>
          </motion.div>

          <div className="cards-grid">
            <motion.div whileHover={{ y: -5 }} className="metric-card">
              <div className="card-header">
                <Activity size={20} color="#8A64D6" />
                <h3>Completion Rate</h3>
              </div>
              <div className="metric-value">{analyticsData?.habitCompletionRate || 0}%</div>
              <p className="panel-subtitle">Current overall consistency</p>
            </motion.div>

            <motion.div whileHover={{ y: -5 }} className="metric-card">
              <div className="card-header">
                <TrendingUp size={20} color="#76A670" />
                <h3>Active Streak</h3>
              </div>
              <div className="metric-value">{analyticsData?.streakCount || 0} Days</div>
              <p className="panel-subtitle">Keep up the momentum!</p>
            </motion.div>

            <motion.div whileHover={{ y: -5 }} className="metric-card">
              <div className="card-header">
                <Zap size={20} color="#F59E0B" />
                <h3>Focus Level</h3>
              </div>
              <div className="metric-value">
                {analyticsData?.focusLevel || 0}
              </div>
              <p className="panel-subtitle">Based on your activity frequency</p>
            </motion.div>
          </div>

          <div className="chart-grid">
            <motion.div
              initial={{ opacity: 0, scale: 0.95 }}
              animate={{ opacity: 1, scale: 1 }}
              transition={{ delay: 0.1 }}
              className="chart-panel"
            >
              <div className="panel-header">
                <h2>Mood Over Time</h2>
                <span className="panel-tag">
                  {analyticsData?.weeklyOutcomeType === 'positive' ? 'Positive Flow' : 'Growth Phase'}
                </span>
              </div>
              <div className="chart-container" style={{ height: '250px' }}>
                <Line data={moodChartData} options={{
                  responsive: true,
                  maintainAspectRatio: false,
                  scales: { y: { min: 0, max: 5 } }
                }} />
              </div>
            </motion.div>

            <motion.div
              initial={{ opacity: 0, scale: 0.95 }}
              animate={{ opacity: 1, scale: 1 }}
              transition={{ delay: 0.2 }}
              className="chart-panel"
            >
              <div className="panel-header">
                <h2>Habit Activity</h2>
                <span className="panel-tag">Stable</span>
              </div>
              <div className="chart-container" style={{ height: '250px' }}>
                <Bar data={habitChartData} options={{
                  responsive: true,
                  maintainAspectRatio: false,
                  scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } }
                }} />

              </div>
            </motion.div>
          </div>

          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.3 }}
            className="buddy-banner"
          >
            <div className="buddy-copy">
              <h3>Mello Buddy's Observation</h3>
              <p>"{analyticsData?.weeklyInsight || 'Start tracking to see my observations!'}"</p>
              <div className="insight-list">
                <p>💡 Regular tracking helps me understand you better.</p>
                <p>💡 Consistency in moods is a sign of emotional stability.</p>
              </div>
            </div>
            <div className="buddy-figure">
              <img src="/mello_bud.png" alt="Mello Buddy" className="buddy-img" />
            </div>
          </motion.div>
        </div>
      </div>
    </div>
  );
}

export default Analytics;
