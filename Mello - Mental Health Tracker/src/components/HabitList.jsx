import { CheckCircle, Circle } from 'lucide-react';

function HabitList() {
  const habits = [
    { id: 1, text: 'Drink water', completed: true },
    { id: 2, text: 'Morning meditation', completed: true },
    { id: 3, text: 'Exercise', completed: true },
    { id: 4, text: 'Read book', completed: false },
    { id: 5, text: 'Write in journal', completed: false },
  ];

  return (
    <div className="card" style={{ flex: 1 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
        <h3>Today's habits</h3>
      </div>
      <ul style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
        {habits.map((habit) => (
          <li key={habit.id} style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
            {habit.completed ? (
              <CheckCircle size={20} color="var(--color-green-bright)" />
            ) : (
              <Circle size={20} color="var(--text-muted)" />
            )}
            <span style={{ 
              textDecoration: habit.completed ? 'line-through' : 'none', 
              color: habit.completed ? 'var(--text-muted)' : 'var(--text-main)' 
            }}>
              {habit.text}
            </span>
          </li>
        ))}
      </ul>
      <button style={{ color: 'var(--color-primary)', fontWeight: '600', marginTop: '1rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
        + Add habit
      </button>
    </div>
  );
}
export default HabitList;
