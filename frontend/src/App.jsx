import { BrowserRouter, Routes, Route} from 'react-router-dom'
import HomePage  from './pages/HomePage'
import LobbyPage from './pages/LobbyPage'
import GamePage from './pages/GamePage'
import AuthPage from './pages/AuthPage'
import './App.css';
import { auth, db } from './services/firebase';
import { AuthProvider, useAuth } from './context/AuthContext';

function AppContent() {
  const { currentUser } = useAuth();

  return (
      <BrowserRouter>
        <div className="app">
          <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center'}}>
              <h1>Codenames</h1>
              { currentUser && (
                  <div style = {{ padding: '10px' }}>
                      Logged in as: {currentUser.email}
                  </div>
              )}
          </div>
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/lobby/:gameId" element={<LobbyPage />} />
            <Route path="/game/:gameId" element={<GamePage />} />
            <Route path="/auth" element={<AuthPage />} />
          </Routes>
        </div>
      </BrowserRouter>
  );
}

function App() {
    return (
        <AuthProvider>
            <AppContent />
        </AuthProvider>
    )
}

export default App;