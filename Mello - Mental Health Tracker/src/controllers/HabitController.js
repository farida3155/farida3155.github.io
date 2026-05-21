import { 
  fetchHabits as fetchHabitsApi, 
  createHabit as createHabitApi,
  deleteHabit as deleteHabitApi,
  updateHabit as updateHabitApi,
  logHabitActivity as logHabitActivityApi,
  fetchTodayLogs as fetchTodayLogsApi
} from "../services/habitService";

export const fetchHabits = async (userId) => {
  return await fetchHabitsApi(userId);
};

export const createHabit = async (habitData) => {
  return await createHabitApi(habitData);
};

export const deleteHabit = async (id) => {
  return await deleteHabitApi(id);
};

export const updateHabit = async (id, habitData) => {
  return await updateHabitApi(id, habitData);
};

export const logHabitActivity = async (logData) => {
  return await logHabitActivityApi(logData);
};

export const fetchTodayLogs = async (userId) => {
  return await fetchTodayLogsApi(userId);
};