import { BrowserRouter, Routes, Route, useNavigate, useLocation } from 'react-router-dom'
import HomePage from './pages/HomePage'
import LobbyPage from './pages/LobbyPage'
import GamePage from './pages/GamePage'
import AuthPage from './pages/AuthPage'
import './App.css';
import { AuthProvider, useAuth } from './context/AuthContext';

function AppHeader() {
    const { currentUser, logout } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();

    const handleLogout = async () => {
        await logout();
        navigate('/auth');
    };

    const handleHome = () => {
        navigate('/');
    };

    return (
        <div className="app-header">
            <div className="header-content">
                <h1 className="app-title" onClick={handleHome} style={{ cursor: 'pointer' }}>
                    Codenames
                </h1>
                <div className="header-actions">
                    {currentUser && (
                        <>
                            <span className="user-email">{currentUser.email}</span>
                            <button className="btn-logout" onClick={handleLogout}>
                                Logout
                            </button>
                        </>
                    )}
                </div>
            </div>
        </div>
    );
}

function AppContent() {
    const { currentUser, loading } = useAuth();

    if (loading) {
        return <div className="loading-screen">Loading...</div>;
    }

    return (
        <BrowserRouter>
            <div className="app">
                <AppHeader />
                <div className="app-content">
                    <Routes>
                        <Route path="/" element={<HomePage />} />
                        <Route path="/lobby/:gameId" element={<LobbyPage />} />
                        <Route path="/game/:gameId" element={<GamePage />} />
                        <Route path="/auth" element={<AuthPage />} />
                    </Routes>
                </div>
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