import React, { useEffect, useMemo, useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { Bell, CheckCircle2, Sparkles, Clock3 } from "lucide-react";
import Sidebar from "../components/Sidebar";
import { fetchNotifications, markNotificationRead } from "../controllers/NotificationController.notification";
import "../styles/NotificationsPage.notification.css";

const FILTERS = [
  { id: "all", label: "All" },
  { id: "unread", label: "Unread" },
];

function typeLabel(type) {
  if (!type) return "General";
  const normalized = String(type).toUpperCase();
  if (normalized === "WELLNESS_QUOTE") return "Wellness";
  if (normalized === "HABIT_REMINDER") return "Reminder";
  if (normalized === "HABIT_DONE") return "Completed";
  if (normalized === "HABIT_MISSED") return "Missed";
  if (normalized === "AFK_3_DAYS") return "Check-in";
  return "Update";
}

function NotificationsPage() {
  const [user, setUser] = useState(null);
  const [notifications, setNotifications] = useState([]);
  const [activeFilter, setActiveFilter] = useState("all");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const savedUser = localStorage.getItem("user");
    if (savedUser) setUser(JSON.parse(savedUser));
  }, []);

  const unreadCount = useMemo(
    () => notifications.filter((n) => !n.read).length,
    [notifications]
  );
  const filteredNotifications = useMemo(() => {
    if (activeFilter === "unread") return notifications.filter((n) => !n.read);
    return notifications;
  }, [notifications, activeFilter]);

  const load = async (userId) => {
    setLoading(true);
    setError("");
    try {
      const data = await fetchNotifications(userId);
      setNotifications(Array.isArray(data) ? data : []);
    } catch (e) {
      setError(e?.message || "Failed to load notifications");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (user?.id) load(user.id);
  }, [user?.id]);

  useEffect(() => {
    if (!user?.id) return undefined;
    const interval = setInterval(() => load(user.id), 30000);
    return () => clearInterval(interval);
  }, [user?.id]);

  const handleMarkRead = async (id) => {
    try {
      const updated = await markNotificationRead(id);
      setNotifications((prev) => prev.map((n) => (n.id === updated.id ? updated : n)));
    } catch (e) {
      setError(e?.message || "Failed to mark read");
    }
  };

  return (
    <div className="dashboard habits">
      <div className="app-container habit-pure-page">
        <Sidebar />

        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          className="content-area no-scrollbar notifications-page"
        >
          <div className="notifications-hero">
            <div className="notifications-hero-left">
              <div className="notifications-hero-icon">
                <Bell size={26} />
              </div>
              <div className="notifications-hero-text">
                <h1>Notifications</h1>
                <p>
                  {unreadCount} unread {unreadCount === 1 ? "notification" : "notifications"}
                </p>
              </div>
            </div>
            <div className="notifications-hero-right">
              <Sparkles size={16} />
              <span>Live updates every 30s</span>
            </div>
          </div>

          <div className="notifications-toolbar">
            {FILTERS.map((filter) => (
              <button
                key={filter.id}
                type="button"
                onClick={() => setActiveFilter(filter.id)}
                className={`notifications-filter-btn ${activeFilter === filter.id ? "active" : ""}`}
              >
                {filter.label}
                {filter.id === "unread" && unreadCount > 0 ? (
                  <span className="notifications-filter-count">{unreadCount}</span>
                ) : null}
              </button>
            ))}
          </div>

          <div className="wellness-main-view">
            <div className="habit-tracking-section notifications-section">
              <div className="section-header-pastel">
                <h2>{activeFilter === "all" ? "All notifications" : "Unread notifications"}</h2>
                <div className="pastel-accent-line" />
              </div>

              {error && (
                <div className="glass-panel notifications-state-panel notifications-error-panel">
                  <p>{error}</p>
                </div>
              )}

              {loading ? (
                <div className="glass-panel notifications-state-panel">
                  <p>Loading notifications...</p>
                </div>
              ) : filteredNotifications.length === 0 ? (
                <div className="glass-panel notifications-state-panel">
                  <p>
                    {activeFilter === "all"
                      ? "No notifications yet. Your habit updates and wellness quotes will appear here."
                      : "You're all caught up. No unread notifications."}
                  </p>
                </div>
              ) : (
                <motion.div layout className="notifications-list">
                  <AnimatePresence>
                    {filteredNotifications.map((n) => (
                      <motion.div
                        key={n.id}
                        layout
                        initial={{ opacity: 0, y: 10 }}
                        animate={{ opacity: 1, y: 0 }}
                        exit={{ opacity: 0, y: -10 }}
                        className={`glass-panel notification-card ${n.read ? "read" : "unread"}`}
                      >
                        <div className="notification-card-main">
                          <div className="notification-card-top">
                            <span className={`notification-type-badge type-${String(n.type || "general").toLowerCase()}`}>
                              {typeLabel(n.type)}
                            </span>
                            <div className="notification-meta-right">
                              <span className="notification-time">
                                <Clock3 size={14} />
                                {n.deliveredAt ? new Date(n.deliveredAt).toLocaleString() : ""}
                              </span>
                              {n.read ? (
                                <span className="notification-read-indicator" title="Read">
                                  <CheckCircle2 size={16} />
                                </span>
                              ) : (
                                <span
                                  className="notification-unread-indicator"
                                  title="Unread"
                                  aria-label="Unread notification"
                                />
                              )}
                            </div>
                          </div>
                          <p className="notification-title">
                            {n.title || "Notification"}
                          </p>
                          <p className="notification-message">
                            {n.message || ""}
                          </p>
                        </div>

                        {!n.read && (
                          <motion.button
                            whileHover={{ scale: 1.03 }}
                            whileTap={{ scale: 0.98 }}
                            className="glass-button notification-mark-read-btn"
                            onClick={() => handleMarkRead(n.id)}
                          >
                            <CheckCircle2 size={18} />
                            Mark read
                          </motion.button>
                        )}
                      </motion.div>
                    ))}
                  </AnimatePresence>
                </motion.div>
              )}
            </div>
          </div>
        </motion.div>
      </div>
    </div>
  );
}

export default NotificationsPage;

