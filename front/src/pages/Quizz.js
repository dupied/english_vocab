import React, { useState, useEffect } from 'react';
import { getRandomWord, updateWordNote } from '../services/api';

function Quizz() {
  const [currentWord, setCurrentWord] = useState(null);
  const [userAnswer, setUserAnswer] = useState('');
  const [showResult, setShowResult] = useState(false);
  const [isCorrect, setIsCorrect] = useState(false);
  const [loading, setLoading] = useState(true);

  const fetchNewWord = async () => {
    setLoading(true);
    setShowResult(false);
    setUserAnswer('');
    setIsCorrect(false);
    try {
      const word = await getRandomWord();
      setCurrentWord(word);
    } catch (error) {
      console.error("Error fetching word", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchNewWord();
  }, []);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!currentWord) return;

    const correct = userAnswer.trim().toLowerCase() === currentWord.word_en.toLowerCase();
    setIsCorrect(correct);
    setShowResult(true);

    if (correct) {
        // Si juste, la note passe à 3 automatiquement
        updateWordNote(currentWord.id, 3);
    }
  };

  const handleManualNoteUpdate = async (note) => {
      if (currentWord) {
          await updateWordNote(currentWord.id, note);
          alert(`Note mise à jour à ${note}`);
      }
  };

  if (loading) return <div>Chargement...</div>;
  if (!currentWord) return <div>Erreur de chargement.</div>;

  return (
    <div style={{ padding: '20px' }}>
      <h2>Quizz</h2>
      
      {!showResult ? (
        <div className="question-section">
          <p>Comment dit-on en anglais :</p>
          <h3>{currentWord.word_fr}</h3>
          <form onSubmit={handleSubmit}>
            <input 
              type="text" 
              value={userAnswer}
              onChange={(e) => setUserAnswer(e.target.value)}
              placeholder="Votre réponse..."
              autoFocus
            />
            <button type="submit">Valider</button>
          </form>
        </div>
      ) : (
        <div className="result-section">
          <h3 style={{ color: isCorrect ? 'green' : 'red' }}>
            {isCorrect ? "Correct !" : "Incorrect !"}
          </h3>
          <p>Le mot était : <strong>{currentWord.word_en}</strong></p>
          <p>Exemple : <em>{currentWord.example_en}</em></p>
          
          {isCorrect ? (
             <p>Note mise à jour à 3 automatiquement.</p>
          ) : (
             <div className="manual-rating">
                 <p>Choisissez une note pour ce mot :</p>
                 <button onClick={() => handleManualNoteUpdate(1)}>1 (À revoir)</button>
                 <button onClick={() => handleManualNoteUpdate(2)}>2 (Moyen)</button>
                 <button onClick={() => handleManualNoteUpdate(3)}>3 (Acquis)</button>
             </div>
          )}

          <button onClick={fetchNewWord} style={{ marginTop: '20px' }}>Mot suivant</button>
        </div>
      )}
    </div>
  );
}

export default Quizz;
