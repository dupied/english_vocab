import  { useState, useEffect } from 'react';
import { getWords } from '../services/api';

function Learn() {
  const [words, setWords] = useState([]);
  const [filter, setFilter] = useState('all');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        const noteFilter = filter === 'all' ? null : filter;
        const data = await getWords(noteFilter);
        setWords(data);
      } catch (error) {
        console.error("Error fetching words", error);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [filter]);

  return (
    <div style={{ padding: '20px' }}>
      <h2>Apprendre le vocabulaire</h2>
      
      <div className="filters" style={{ marginBottom: '20px' }}>
        <span>Filtrer par note : </span>
        <button onClick={() => setFilter('all')} disabled={filter === 'all'}>Tous</button>
        <button onClick={() => setFilter('1')} disabled={filter === '1'}>Note 1</button>
        <button onClick={() => setFilter('2')} disabled={filter === '2'}>Note 2</button>
        <button onClick={() => setFilter('3')} disabled={filter === '3'}>Note 3</button>
      </div>

      {loading ? (
        <p>Chargement...</p>
      ) : (
        <table border="1" style={{ width: '100%', borderCollapse: 'collapse' }}>
          <thead>
            <tr>
              <th>Anglais</th>
              <th>Français</th>
              <th>Exemple</th>
              <th>Note</th>
            </tr>
          </thead>
          <tbody>
            {words.map(word => (
              <tr key={word.id}>
                <td><strong>{word.word_en}</strong></td>
                <td>{word.word_fr}</td>
                <td><em>{word.example_en}</em></td>
                <td>{word.note}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default Learn;
