function MoodSelector() {
  const moods = [
    { label: 'Happy', emoji: '😄', color: 'var(--color-green-bright)' },
    { label: 'Calm', emoji: '😌', color: 'var(--color-green-soft)' },
    { label: 'Sad', emoji: '😔', color: 'var(--color-blue)' },
    { label: 'Anxious', emoji: '😖', color: 'var(--color-purple)' },
    { label: 'Tired', emoji: '🥱', color: 'var(--color-red)' },
  ];

  return (
    <div className="card">
      <h3>How do you feel today?</h3>
      <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '1rem', gap: '1rem' }}>
        {moods.map((mood) => (
          <div key={mood.label} style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '0.5rem', cursor: 'pointer', flex: 1 }}>
            <div style={{ 
              width: '60px', height: '60px', borderRadius: '50%', backgroundColor: mood.color, 
              display: 'flex', justifyContent: 'center', alignItems: 'center', fontSize: '1.75rem',
              transition: 'transform 0.2s', boxShadow: 'var(--shadow-sm)'
            }}
            onMouseOver={(e) => e.currentTarget.style.transform = 'scale(1.1)'}
            onMouseOut={(e) => e.currentTarget.style.transform = 'scale(1)'}
            >
              {mood.emoji}
            </div>
            <span style={{ fontSize: '0.85rem', color: 'var(--text-muted)', fontWeight: '500' }}>{mood.label}</span>
          </div>
        ))}
      </div>
    </div>
  );
}
export default MoodSelector;
