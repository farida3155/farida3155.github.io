import API_BASE from '../utils/api.js';


export async function fetchNotifications(userId) {
  const res = await fetch(`${API_BASE}/api/notifications?userId=${encodeURIComponent(userId)}`);
  if (!res.ok) throw new Error("Failed to fetch notifications");
  return await res.json();
}

export async function markNotificationRead(notificationId) {
  const res = await fetch(`${API_BASE}/api/notifications/${encodeURIComponent(notificationId)}/read`, {
    method: "POST",
  });
  if (!res.ok) throw new Error("Failed to mark notification as read");
  return await res.json();
}

