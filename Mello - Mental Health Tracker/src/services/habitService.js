import API_BASE from '../utils/api.js';
const API_URL = `${API_BASE}/api/habits`;

export const fetchHabits = async (userId) => {
  try {
    const response = await fetch(`${API_URL}?userId=${userId}`);
    if (!response.ok) {
      throw new Error("Failed to fetch habits");
    }
    return await response.json();
  } catch (error) {
    console.error("Error fetching habits:", error);
    return [];
  }
};

export const createHabit = async (habitData) => {
  try {
    const response = await fetch(API_URL, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(habitData),
    });
    if (!response.ok) {
      throw new Error("Failed to create habit");
    }
    return await response.json();
  } catch (error) {
    console.error("Error creating habit:", error);
    return null;
  }
};

export const deleteHabit = async (id) => {
  try {
    const response = await fetch(`${API_URL}/${id}`, {
      method: "DELETE",
    });
    return response.ok;
  } catch (error) {
    console.error("Error deleting habit:", error);
    return false;
  }
};

export const updateHabit = async (id, habitData) => {
  try {
    const response = await fetch(`${API_URL}/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(habitData),
    });
    if (!response.ok) {
      throw new Error("Failed to update habit");
    }
    return await response.json();
  } catch (error) {
    console.error("Error updating habit:", error);
    return null;
  }
};

export const logHabitActivity = async (logData) => {
  try {
    const response = await fetch(`${API_BASE}/api/habit-logs`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(logData),
    });
    return await response.json();
  } catch (error) {
    console.error("Error logging activity:", error);
    return null;
  }
};

export const fetchTodayLogs = async (userId) => {
  try {
    const response = await fetch(`${API_BASE}/api/habit-logs/today?userId=${userId}`);
    return await response.json();
  } catch (error) {
    console.error("Error fetching today logs:", error);
    return [];
  }
};