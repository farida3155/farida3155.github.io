import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { motion, AnimatePresence } from "framer-motion";
import {
  LayoutDashboard,
  Smile,
  CheckSquare,
  BarChart2,
  Sparkles,
  Settings,
  Heart,
  Bell,
  User as UserIcon,
  LogOut,
  X,
  Check
} from "lucide-react";
import "../styles/sidebar.css";

function Sidebar() {
  const navigate = useNavigate();
  const location = useLocation();
  const [showLogoutConfirm, setShowLogoutConfirm] = useState(false);
  const [user, setUser] = useState(null);

  // Sync user data on mount and when localStorage changes
  useEffect(() => {
    const fetchUser = () => {
      try {
        const saved = localStorage.getItem('user');
        if (saved) setUser(JSON.parse(saved));
      } catch (e) {
        console.error("Failed to parse user", e);
      }
    };

    fetchUser();

    // Listen for storage changes (works across tabs/windows)
    window.addEventListener('storage', fetchUser);

    // Also set a small interval to catch same-page updates if needed
    const interval = setInterval(fetchUser, 1000);

    return () => {
      window.removeEventListener('storage', fetchUser);
      clearInterval(interval);
    };
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("user");
    navigate("/auth");
  };

  const menuItems = [
    { name: "Dashboard", icon: <LayoutDashboard size={22} />, path: "/dashboard" },
    { name: "Mood", icon: <Smile size={22} />, path: "/mood" },
    { name: "Habits", icon: <CheckSquare size={22} />, path: "/habits" },
    { name: "Notifications", icon: <Bell size={22} />, path: "/notifications" },
    { name: "Analytics", icon: <BarChart2 size={22} />, path: "/analytics" },
    { name: "Recommendations", icon: <Sparkles size={22} />, path: "/recommendations" },
  ];

  const isActive = (item) => {
    if (item.path === "/dashboard") {
      return location.pathname === "/dashboard" || location.pathname === "/";
    }
    return location.pathname.startsWith(item.path);
  };

  return (
    <motion.div
      initial={{ x: -100, opacity: 0 }}
      animate={{ x: 0, opacity: 1 }}
      className="sidebar"
    >
      <div className="sidebar-scroll-container">
        <div className="logo">
          <div className="logo-icon">
            <img src="/mellologo2.png" alt="Mello Logo" style={{ width: '100%', height: '100%', objectFit: 'contain' }} />
          </div>
        </div>

        <ul className="menu-list">
          {menuItems.map((item, index) => {
            const active = isActive(item);
            return (
              <motion.li
                key={index}
                className={active ? "active" : ""}
                whileHover={!active ? { x: 4, opacity: 0.8 } : {}}
                whileTap={{ scale: 0.98 }}
                onClick={() => navigate(item.path)}
              >
                {item.icon}
                <span>{item.name}</span>
                {active && (
                  <motion.div
                    layoutId="active-pill"
                    className="active-indicator"
                  />
                )}
              </motion.li>
            );
          })}
        </ul>

        <div className="sidebar-footer">

          {/* User Profile Preview */}
          <div className="user-preview" onClick={() => navigate('/profile')}>
            <div className="user-avatar-container">
              {user?.profilePicture ? (
                <img src={user.profilePicture} alt="Profile" className="user-avatar-img" />
              ) : (
                <UserIcon size={20} color="var(--color-primary)" />
              )}
            </div>
            <span className="user-name-text">
              {user?.fullName || 'User'}
            </span>
          </div>

          {/* Logout Section with Confirmation */}
          <div className="logout-section">
            <AnimatePresence mode="wait">
              {!showLogoutConfirm ? (
                <motion.button
                  key="logout-btn"
                  initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }}
                  className="logout-btn"
                  onClick={() => setShowLogoutConfirm(true)}
                >
                  <LogOut size={20} />
                  <span>Log out</span>
                </motion.button>
              ) : (
                <motion.div
                  key="confirm-box"
                  initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} exit={{ opacity: 0, y: 10 }}
                  className="logout-confirm-box"
                >
                  <p>Are you sure?</p>
                  <div className="confirm-btns">
                    <button onClick={handleLogout} className="btn-yes"><Check size={16} /></button>
                    <button onClick={() => setShowLogoutConfirm(false)} className="btn-no"><X size={16} /></button>
                  </div>
                </motion.div>
              )}
            </AnimatePresence>
          </div>
        </div>
      </div>
    </motion.div>
  );
}

export default Sidebar;
