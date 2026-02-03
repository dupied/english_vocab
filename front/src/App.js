import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import './App.css';
import Home from './pages/Home';
import Quizz from './pages/Quizz';
import Learn from './pages/Learn';

function App() {
  return (
    <Router>
      <div className="App">
        <header className="App-header-mini">
             <nav>
                 <Link to="/" style={{ color: 'white', marginRight: '15px' }}>Accueil</Link>
                 <Link to="/quizz" style={{ color: 'white', marginRight: '15px' }}>Quizz</Link>
                 <Link to="/apprendre" style={{ color: 'white' }}>Apprendre</Link>
             </nav>
        </header>

        <main className="App-content">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/quizz" element={<Quizz />} />
            <Route path="/apprendre" element={<Learn />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
