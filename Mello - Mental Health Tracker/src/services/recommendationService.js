import API_BASE from '../utils/api.js';
const API_URL = `${API_BASE}/api/recommendations`;

export const fetchRecommendations = async (mood, userId) => {
  try {
    const response = await fetch(`${API_URL}?mood=${mood}&userId=${userId}`);
    if (!response.ok) {
      throw new Error("Failed to fetch recommendations");
    }
    return await response.json();
  } catch (error) {
    console.error("Error fetching recommendations:", error);
    return null;
  }
};

export const completeActivity = async (activity, mood) => {
  try {
    const response = await fetch(`${API_URL}/complete?activity=${activity}&mood=${mood}`, {
      method: "POST",
    });
    return await response.json();
  } catch (error) {
    console.error("Error completing activity:", error);
    return null;
  }
};
