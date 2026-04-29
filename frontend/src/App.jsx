import { BrowserRouter, Routes, Route, Link, useNavigate, useLocation } from 'react-router-dom'
import HomePage  from './pages/HomePage'
import LobbyPage from './pages/LobbyPage'
import GamePage from './pages/GamePage'
import AuthPage from './pages/AuthPage'
import RulesPage from './pages/RulesPage'
import './App.css';
import { auth, db } from './services/firebase';
import { AuthProvider, useAuth } from './context/AuthContext';
import { api } from './services/api';

function AppContent() {
  const { currentUser, logout } = useAuth();
  const { playerId } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = async () => { // TODO: ask "are you sure?"
    try {
      //take game ID from URL and leave game before logging out
      const match = location.pathname.match(/\/(game|lobby)\/(\d+)/);
      if (match && playerId) {
        const gameId = match[2];
        await api.leaveGame(gameId, playerId);
      }
    } catch (error) {
      console.error('Error leaving game:', error);
    }
    
    await logout();
    navigate('/auth');
  };

  const handleGoHome = async (e) => { // TODO: ask "are you sure?" and set game status to ended
    e.preventDefault();
    try {
      // Leave game if currently in one before going home
      const match = location.pathname.match(/\/(game|lobby)\/(\d+)/);
      if (match && playerId) {
        const gameId = match[2];
        await api.leaveGame(gameId, playerId);
      }
    } catch (error) {
      console.error('Error leaving game:', error);
    }
    navigate('/');
  };

  return (
    <div className="app">
      <header style={{
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        padding: '16px 24px',
        borderBottom: '1px solid #e5e7eb',
        backgroundColor: '#fff',
        position: 'sticky',
        top: 0,
        zIndex: 100
      }}>
        <a href="/" onClick={handleGoHome} style={{ textDecoration: 'none', color: 'inherit', cursor: 'pointer' }}>
          <h1 style={{ margin: 0, fontSize: '24px', fontWeight: 'bold' }}>Codenames</h1>
        </a>

        {currentUser && (
          <div style={{ display: 'flex', alignItems: 'center', gap: '24px' }}>
            <span style={{ color: '#666', fontSize: '14px' }}>
              Logged in as: <strong>{currentUser.email}</strong>
            </span>
            <button
              className="logout-button"
              onClick={handleLogout}
              style={{ padding: '8px 16px', cursor: 'pointer' }}
            >
              Logout
            </button>
          </div>
        )}
      </header>

      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/rules" element={<RulesPage />} />
        <Route path="/lobby/:gameId" element={<LobbyPage />} />
        <Route path="/game/:gameId" element={<GamePage />} />
        <Route path="/auth" element={<AuthPage />} />
      </Routes>

    </div>
  );
}

function App() {
    return (
        <BrowserRouter>
            <AuthProvider>
                <AppContent />
            </AuthProvider>
        </BrowserRouter>
    )
}

export default App;